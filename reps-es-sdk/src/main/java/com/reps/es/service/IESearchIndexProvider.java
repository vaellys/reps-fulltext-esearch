package com.reps.es.service;

import java.util.List;
import java.util.Map;

import com.reps.es.entity.ConfigParam;
import com.reps.es.entity.FieldKey;
import com.reps.es.exception.ElasticsearchException;

public interface IESearchIndexProvider {
	
	/**
	 * 批量索引文档
	 * @param configParam 参数设置
	 * @param documents 索引文档内容
	 * @param keyMapper 键映射
	 * @return boolean
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(ConfigParam configParam, Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException;
	
	/**
	 * 通过ID删除
	 * @param configParam
	 * @param ids
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean delete(ConfigParam configParam, List<String> ids) throws ElasticsearchException;
	
}
