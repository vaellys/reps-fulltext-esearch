package com.reps.es.sdk.service;

import java.util.List;
import java.util.Map;

import com.reps.core.orm.ListResult;
import com.reps.es.bean.ContentVo;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.sdk.config.ProviderConfig;
import com.reps.es.sdk.config.FieldKey;

public interface IESearchIndexProvider {
	
	/**
	 * 批量索引文档 (用于全文检索)
	 * @param documents 索引文档内容
	 * @return boolean
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(List<ContentVo> documents) throws ElasticsearchException;
	
	/**
	 * 批量索引文档
	 * @param documents 索引文档内容
	 * @param keyMapper 键映射
	 * @return boolean
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException;
	
	/**
	 * 批量索引文档
	 * @param configParam 参数设置
	 * @param documents 索引文档内容
	 * @param keyMapper 键映射
	 * @return boolean
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(ProviderConfig configParam, Map<String, FieldKey> keyMapper, List<Map<String, ?>> documents) throws ElasticsearchException;
	
	/**
	 * 全文检索接口
	 * @param keywords
	 * @param startRow
	 * @param pageSize
	 * @return
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> search(String keywords, int startRow, int pageSize) throws ElasticsearchException;
	
	/**
	 * @param queryIndexFields 查询字段列表
	 * @param keywords 查询关键词
	 * @param startRow 开始行
	 * @param pageSize 页大小
	 * @return ListResult<Map<String, ?>>
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> search(String[] queryIndexFields, String keywords, int startRow, int pageSize) throws ElasticsearchException;
	
	/**
	 * @param providerConfig 查询参数设置
	 * @param queryIndexFields 查询字段列表
	 * @param keywords 查询关键词
	 * @param startRow 开始行
	 * @param pageSize 页大小
	 * @return ListResult<Map<String, ?>>
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> search(ProviderConfig providerConfig, String[] queryIndexFields, String keywords, int startRow, int pageSize) throws ElasticsearchException;
	
	/**
	 * 通过ID删除
	 * @param ids
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean delete(List<String> ids) throws ElasticsearchException;
	
	/**
	 * 通过ID删除
	 * @param configParam
	 * @param ids
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean delete(ProviderConfig configParam, List<String> ids) throws ElasticsearchException;

	
}
