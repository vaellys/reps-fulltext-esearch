package com.reps.es.sdk.service.impl;

import static com.reps.es.sdk.enums.FullText.INDEX;
import static com.reps.es.sdk.enums.FullText.TYPE;
import static com.reps.es.sdk.util.ContentFieldMapper.buildKeyMapper;
import static com.reps.es.util.PageUtil.cps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.es.bean.ContentVo;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.sdk.config.FieldKey;
import com.reps.es.sdk.config.ProviderConfig;
import com.reps.es.sdk.service.IESearchIndexProvider;
import com.reps.es.sdk.util.ContentFieldMapper;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.util.QueryParam;

@Service
public class ESearchIndexProviderImpl implements IESearchIndexProvider {

	protected Logger logger = LoggerFactory.getLogger(ESearchIndexProviderImpl.class);

	public static final Map<String, Object> INDEX_SETTINGS = new HashMap<>();

	static {
		INDEX_SETTINGS.put("index.number_of_shards", 3);
		INDEX_SETTINGS.put("index.number_of_replicas", 0);
	}

	@Autowired
	private IElasticsearchService elasticsearchService;

	@Autowired
	private ESearchConfigManager esManager;

	@Autowired
	private ProviderConfig providerConfig;

	@SuppressWarnings("unchecked")
	@Override
	public boolean addIndex(List<ContentVo> documents) throws ElasticsearchException {
		// 索引初始化配置检查
		checkConfigInitialization();
		if (null == documents) {
			throw new ElasticsearchException("索引文档内容不能为空");
		}
		try {
			configIpAndPort(this.providerConfig.getIp(), this.providerConfig.getPort());
			for (ContentVo contentVo : documents) {
				contentVo.setTime(new Date());
			}
			ObjectMapper objectMapper = new ObjectMapper();
			return executeIndex(buildKeyMapper(), objectMapper.readValue(objectMapper.writeValueAsBytes(documents), List.class), INDEX.getCode(), TYPE.getCode(), false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ElasticsearchException("索引异常", e);
		}
	}

	@Override
	public boolean addIndex(ProviderConfig configParam, Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException {
		if (null == configParam || null == keyMapper || null == documents) {
			throw new ElasticsearchException("参数异常");
		}
		checkParams(configParam);
		String index = configParam.getIndex();
		String type = configParam.getType();
		String ip = configParam.getIp();
		Integer port = configParam.getPort();
		configIpAndPort(ip, port);
		return executeIndex(keyMapper, documents, index, type, true);
	}

	@Override
	public boolean addIndex(Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException {
		checkConfigInitialization();
		if (null == keyMapper || null == documents) {
			throw new ElasticsearchException("参数异常");
		}
		checkParams(this.providerConfig);
		String index = this.providerConfig.getIndex();
		String type = this.providerConfig.getType();
		String ip = this.providerConfig.getIp();
		Integer port = this.providerConfig.getPort();
		configIpAndPort(ip, port);
		return executeIndex(keyMapper, documents, index, type, true);
	}

	private void checkParams(ProviderConfig configParam) throws ElasticsearchException {
		if (StringUtil.isBlank(configParam.getIndex())) {
			throw new ElasticsearchException("索引名称不能为空");
		}
		if (StringUtil.isBlank(configParam.getType())) {
			throw new ElasticsearchException("索引类型不能为空");
		}
		if (StringUtil.isBlank(configParam.getIp())) {
			throw new ElasticsearchException("ip不能为空");
		}
		if (null == configParam.getPort()) {
			throw new ElasticsearchException("端口不能为空");
		}
	}

	private void checkConfigInitialization() throws ElasticsearchException {
		if (null == this.providerConfig) {
			throw new ElasticsearchException("参数异常:ES参数配置未初始化");
		}
	}

	private void configIpAndPort(String ip, Integer port) {
		esManager.setHost(ip);
		esManager.setPort(port);
	}

	private boolean executeIndex(Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents, String index, String type, boolean hasCheckField) throws ElasticsearchException {
		if (documents.isEmpty()) {
			throw new ElasticsearchException("索引数据不能为空");
		}
		// 设置类型映射
		Map<String, String> typeMap = new HashMap<>();
		typeMap.put(type, JSON.toJSONString(keyMapper));
		if (!elasticsearchService.checkIndexType(index, type)) {
			elasticsearchService.addIndexMapping(index, type, INDEX_SETTINGS, typeMap);
		}
		if (hasCheckField) {
			// 获取映射键列表
			Set<String> keys = keyMapper.keySet();
			// 移除不需要建索引的字段
			List<Map<String, ?>> list = new ArrayList<>();
			if (!documents.isEmpty()) {
				for (Map<String, ?> map : documents) {
					Map<String, Object> dataMap = new HashMap<>();
					for (String key : keys) {
						if (map.containsKey(key)) {
							Object value = map.get(key);
							dataMap.put(key, value);
						}
					}
					list.add(dataMap);
				}
			}
			if (list.size() > 0) {
				return elasticsearchService.addIndex(index, type, list);
			} else {
				throw new ElasticsearchException("索引异常:映射字段中的内容不能空");
			}
		} else {
			return elasticsearchService.addIndex(index, type, documents);
		}
	}
	
	@Override
	public ListResult<Map<String, ?>> search(String keywords, int startRow, int pageSize) throws ElasticsearchException {
		checkConfigInitialization();
		if (StringUtil.isBlank(keywords)) {
			throw new ElasticsearchException("关键词不能为空");
		}
		providerConfig.setIndex(INDEX.getCode());
		providerConfig.setType(TYPE.getCode());
		// 设置ip + 端口
		configIpAndPort(providerConfig.getIp(), providerConfig.getPort());
		return executeSearch(this.providerConfig,  new String[] {ContentFieldMapper.FIELD_MAPPER_TITLE, ContentFieldMapper.FIELD_MAPPER_SUMMARY}, keywords, startRow, pageSize);
	}

	@Override
	public ListResult<Map<String, ?>> search(ProviderConfig providerConfig, String[] queryIndexFields, String keywords, int startRow, int pageSize) throws ElasticsearchException {
		if (null == providerConfig || null == queryIndexFields) {
			throw new ElasticsearchException("参数异常");
		}
		if (StringUtil.isBlank(keywords)) {
			throw new ElasticsearchException("关键词不能为空");
		}
		checkParams(providerConfig);
		// 设置ip + 端口
		configIpAndPort(providerConfig.getIp(), providerConfig.getPort());
		return executeSearch(providerConfig, queryIndexFields, keywords, startRow, pageSize);
	}

	@Override
	public ListResult<Map<String, ?>> search(String[] queryIndexFields, String keywords, int startRow, int pageSize) throws ElasticsearchException {
		checkConfigInitialization();
		if (null == queryIndexFields) {
			throw new ElasticsearchException("参数异常:未指定搜索字段");
		}
		if (StringUtil.isBlank(keywords)) {
			throw new ElasticsearchException("关键词不能为空");
		}
		checkParams(providerConfig);
		// 设置ip + 端口
		configIpAndPort(providerConfig.getIp(), providerConfig.getPort());
		return executeSearch(this.providerConfig, queryIndexFields, keywords, startRow, pageSize);
	}

	@SuppressWarnings("unchecked")
	private ListResult<Map<String, ?>> executeSearch(ProviderConfig providerConfig, String[] queryIndexFields, String keywords, int startRow, int pageSize) throws ElasticsearchException {
		QueryParam queryParam = new QueryParam();
		queryParam.setIndices(providerConfig.getIndex());
		queryParam.setTypes(providerConfig.getType());
		String fields = StringUtil.join(queryIndexFields, ",");
		queryParam.setQueryFields(fields);
		queryParam.setHightlighterFields(fields);
		queryParam.setHighlighterPreTags(providerConfig.getHighlighterPreTags());
		queryParam.setHighlighterPostTags(providerConfig.getHighlighterPostTags());
		queryParam.setKeywords(keywords);
		ListResult<Map<String, ?>> listResult = elasticsearchService.search(queryParam, startRow, cps(pageSize));
		if (null == listResult) {
			listResult = new ListResult<>();
			listResult.setList(Collections.EMPTY_LIST);
		}
		listResult.setPageSize(pageSize);
		return listResult;
	}

	@Override
	public boolean delete(ProviderConfig configParam, List<String> ids) throws ElasticsearchException {
		if (null == configParam || null == ids) {
			throw new ElasticsearchException("参数异常");
		}
		checkParams(configParam);
		String index = configParam.getIndex();
		String type = configParam.getType();
		String ip = configParam.getIp();
		Integer port = configParam.getPort();
		// 设置ip + 端口
		configIpAndPort(ip, port);
		return executeDelete(ids, index, type);
	}

	@Override
	public boolean delete(List<String> ids) throws ElasticsearchException {
		checkConfigInitialization();
		if (null == ids) {
			throw new ElasticsearchException("参数异常");
		}
		checkParams(this.providerConfig);
		String index = this.providerConfig.getIndex();
		String type = this.providerConfig.getType();
		String ip = this.providerConfig.getIp();
		Integer port = this.providerConfig.getPort();
		// 设置ip + 端口
		configIpAndPort(ip, port);
		return executeDelete(ids, index, type);
	}

	private boolean executeDelete(List<String> ids, String index, String type) throws ElasticsearchException {
		if (ids.size() > 0) {
			for (String id : ids) {
				boolean isFound = elasticsearchService.deleteById(index, type, id);
				if (!isFound) {
					logger.info("删除数据ID不存在 {}", id);
				} else {
					logger.info("删除数据成功ID {}", id);
				}
			}
		}
		return true;
	}

}
