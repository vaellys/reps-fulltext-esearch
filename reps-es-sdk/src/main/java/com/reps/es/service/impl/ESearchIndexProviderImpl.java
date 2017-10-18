package com.reps.es.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.reps.core.util.StringUtil;
import com.reps.es.entity.ConfigParam;
import com.reps.es.entity.FieldKey;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.service.IESearchIndexProvider;

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
	
	@Override
	public boolean addIndex(ConfigParam configParam, Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException {
		if(null == configParam || null == keyMapper || null == documents) {
			throw new ElasticsearchException("参数异常");
		}
		String index = configParam.getIndex();
		if(StringUtil.isBlank(index)) {
			throw new ElasticsearchException("索引名称不能为空");
		}
		String type = configParam.getType();
		if(StringUtil.isBlank(type)) {
			throw new ElasticsearchException("索引类型不能为空");
		}
		String ip = configParam.getIp();
		if(StringUtil.isBlank(ip)) {
			throw new ElasticsearchException("ip不能为空");
		}
		Integer port = configParam.getPort();
		if(null == port) {
			throw new ElasticsearchException("端口不能为空");
		}
		//设置ip + 端口
		esManager.setHost(ip);
		esManager.setPort(port);
		//设置类型映射
		Map<String, String> typeMap = new HashMap<>();
		typeMap.put(type, JSON.toJSONString(keyMapper));
		if(!elasticsearchService.checkIndexType(index, type)) {
			elasticsearchService.addIndexMapping(index, type, INDEX_SETTINGS, typeMap);
		}
		//获取映射键列表
		Set<String> keys = keyMapper.keySet();
		//移除不需要建索引的字段
		List<Map<String, ?>> listResult = new ArrayList<>();
		if(null != documents && documents.size() > 0) {
			for (Map<String, ?> map : documents) {
				Map<String, Object> dataMap = new HashMap<>();
				for (String key : keys) {
					if(map.containsKey(key)) {
						Object value = map.get(key);
						dataMap.put(key, value);
					}
				}
				listResult.add(dataMap);
			}
		}
		if(listResult.size() > 0) {
			return elasticsearchService.addIndex(index, type, listResult);
		} else {
			throw new ElasticsearchException("索引数据不能为空");
		}
	}

	@Override
	public boolean delete(ConfigParam configParam, List<String> ids) throws ElasticsearchException {
		if(null == configParam || null == ids) {
			throw new ElasticsearchException("参数异常");
		}
		String index = configParam.getIndex();
		if(StringUtil.isBlank(index)) {
			throw new ElasticsearchException("索引名称不能为空");
		}
		String type = configParam.getType();
		if(StringUtil.isBlank(type)) {
			throw new ElasticsearchException("索引类型不能为空");
		}
		String ip = configParam.getIp();
		if(StringUtil.isBlank(ip)) {
			throw new ElasticsearchException("ip不能为空");
		}
		Integer port = configParam.getPort();
		if(null == port) {
			throw new ElasticsearchException("端口不能为空");
		}
		//设置ip + 端口
		esManager.setHost(ip);
		esManager.setPort(port);
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
