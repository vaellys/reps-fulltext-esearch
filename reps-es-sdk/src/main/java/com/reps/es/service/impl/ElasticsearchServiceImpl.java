package com.reps.es.service.impl;

import static com.reps.es.util.AnalyzerEnum.IK;
import static com.reps.es.util.IndicesUtil.indexDocument;
import static com.reps.es.util.IndicesUtil.putIndexMapping;
import static com.reps.es.util.IndicesUtil.searchHitField;
import static com.reps.es.util.IndicesUtil.searcher;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.orm.ListResult;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.util.IndicesUtil;
import com.reps.es.util.QueryParam;

@Service
public class ElasticsearchServiceImpl implements IElasticsearchService {
	
	private static Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);
	
	@Autowired
	private ESearchConfigManager esManager;
	
	@Override
	public boolean addIndex(String index, String type, String id, Map<String, ?> document) throws ElasticsearchException {
		return indexDocument(esManager.getClient(), index, type, id, document);
	}

	@Override
	public boolean addIndex(String index, String type, List<Map<String, ?>> documents) throws ElasticsearchException {
		return indexDocument(esManager.getClient(), index, type, documents);
	}
	
	@Override
	public boolean deleteIndex(String index) throws ElasticsearchException {
		return IndicesUtil.deleteIndex(esManager.getClient(), index);
	}
	
	@Override
	public boolean addIndexMapping(String index, String type, Map<String, ?> indexSettings, Map<String, String> mappingSettings) throws ElasticsearchException{
		//若索引类型为空，则根据mappingSettings中key->value进行索引类型设置
		if(StringUtils.isEmpty(type)){
			for (Map.Entry<String, String> mapping : mappingSettings.entrySet()) {
				putIndexMapper(index, mapping.getKey(), indexSettings, mapping.getValue());
			}
		}else{
			//若索引类型不为空，判断映射列表中是否包含相应索引类型名称，然后进行索引类型设置
			if(mappingSettings.containsKey(type)){
				putIndexMapper(index, type, indexSettings, mappingSettings.get(type));
			}else{
				logger.info("索引映射列表 中不包含指定映射类型名称，请检查映射文件名称与传递索引类型是否一致");
				return false;
			}
		}
		return true;
	}
	
	private void putIndexMapper(String index, String type, Map<String, ?> indexSettings, String mappingStr) throws ElasticsearchException{
		//拼接es需要的完整索引类型结构
		StringBuilder sb = new StringBuilder();
		sb.append("{\"properties\":");
		sb.append(mappingStr);
		sb.append("}");
		String value = sb.toString();
		logger.debug("索引类型名称 {}，完整映射结构 {},", type, sb.toString());
		//进行索引类型设置
		boolean flag = putIndexMapping(esManager.getClient(), index, type, indexSettings, value);
		if(flag){ 
			logger.info("映射创建成功: {}", value);
		} else {
			logger.info("映射创建失败: {}", value);
		}
	}
	
	public ListResult<Map<String, ?>> search(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException{
		setQueryFields(queryParam);
		setHighlightFields(queryParam);
		return searcher(esManager.getClient(), queryParam, pageNow, pageSize);
	}
	
	private String getFields(String[] fields) {
		//对于相同的字段添加多个分词
		StringBuilder sb = new StringBuilder();
		for (String field : fields) {
			sb.append(field);
			sb.append(",");
			sb.append(field + IK.getSuffix());
			sb.append(",");
		}
		return sb.toString();
	}

	@Override
	public ListResult<Map<String, ?>> specialSearch(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException {
		setQueryFields(queryParam);
		setHighlightFields(queryParam);
		return searchHitField(esManager.getClient(), queryParam, pageNow, pageSize);
	}

	private void setQueryFields(QueryParam queryParam) {
		String[] queryFields = queryParam.getQueryFields().split(",");
		queryParam.setQueryFields(getFields(queryFields));
	}
	
	private void setHighlightFields(QueryParam queryParam) {
		String[] hightlightFields = queryParam.getHightlighterFields().split(",");
		queryParam.setHightlighterFields(getFields(hightlightFields));
	}

	@Override
	public List<Map<String, Object>> searchAll(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException {
		return IndicesUtil.searchAll(esManager.getClient(), queryParam, pageNow, pageSize);
	}

	@Override
	public ListResult<Map<String, ?>> fuzzySearch(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException {
		//取消高亮
		queryParam.setHightlighterFields("");
		return searcher(esManager.getClient(), queryParam, pageNow, pageSize);
	}

}
