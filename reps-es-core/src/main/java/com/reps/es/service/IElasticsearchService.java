package com.reps.es.service;

import java.util.List;
import java.util.Map;

import com.reps.core.orm.ListResult;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.util.QueryParam;

public interface IElasticsearchService {
	
	/**
	 * 索引单条文档
	 * @param index
	 * @param type
	 * @param id 文档id 若不指定，ES会自动生成
	 * @param document
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(String index, String type, String id, Map<String, ?> document) throws ElasticsearchException;
	
	/**
	 * 批量索引文档
	 * @param index
	 * @param type
	 * @param documents 索引文档内容
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean addIndex(String index, String type, List<Map<String, ?>> documents) throws ElasticsearchException;
	
	/**
	 * 删除索引
	 * @param index
	 * @return boolean
	 * @throws ElasticsearchException
	 */
	public boolean deleteIndex(String index) throws ElasticsearchException;
	
	/**
	 * 通过ID删除
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean deleteById(String index, String type, String id) throws ElasticsearchException;

	/**
	 * 添加索引设置和映射类型
	 * @param index
	 * @param type
	 * @param indexSettings
	 * @param mappingSettings 映射类型设置(json字符串) key为映射类型,value为映射数据结构
	 * @return
	 * @throws ElasticsearchException
	 */
	public boolean addIndexMapping(String index, String type,
			Map<String, ?> indexSettings, Map<String, String> mappingSettings)
			throws ElasticsearchException;

	/**
	 * @param queryParam 查询参数实体
	 * @param pageNow 当前页
	 * @param pageSize 页大小
	 * @return ListResult<Map<String, ?>>
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> search(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException;
	
	/**
	 * 搜索结果返回指定字段
	 * @param queryParam 查询参数实体
	 * @param pageNow 当前页
	 * @param pageSize 页大小
	 * @return ListResult<Map<String, ?>>
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> specialSearch(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException;
	
	/**
	 * 查询所有结果
	 * @param queryParam 查询参数实体
	 * @param pageNow 当前页
	 * @param pageSize 页大小
	 * @return List<Map<String,Object>>
	 * @throws ElasticsearchException
	 */
	public List<Map<String, Object>> searchAll(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException;
	
	/**
	 * 模糊搜索
	 * 此接口不会对关键词进行分词（相当于like查询）
	 * @param queryParam
	 * @param pageNow
	 * @param pageSize
	 * @return ListResult<Map<String, ?>>
	 * @throws ElasticsearchException
	 */
	public ListResult<Map<String, ?>> fuzzySearch(QueryParam queryParam, int pageNow, int pageSize) throws ElasticsearchException;
	
}
