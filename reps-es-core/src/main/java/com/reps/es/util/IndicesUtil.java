package com.reps.es.util;

import static com.reps.es.util.AnalyzerEnum.IK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryAction;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.es.exception.ElasticsearchException;

/**
 * 索引管理操作
 * @ClassName: IndicesUtil
 * @Description: TODO
 * @author: qianguobing
 * @date: 2017年7月22日 下午2:09:04
 * @version: V1.0
 */
public class IndicesUtil {
	
	private IndicesUtil(){
	}
	
	/**
	 * es ID
	 */
	public static final String ES_ID = "id";
	
	private static Logger logger = LoggerFactory.getLogger(IndicesUtil.class);
	
	/**
	 * 判断索引是否存在
	 * @param index
	 * @param client
	 * @return
	 */
	protected static boolean isIndexExists(Client client, String index) {
		if (Objects.equals(client, null)) {
			logger.info("请求索引客户端为null");
			return false;
		}
		if (StringUtils.isBlank(index)) {
			logger.info("索引名称为空");
			return false;
		}
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		IndicesExistsResponse response = indicesAdminClient.prepareExists(index).get();
		return response.isExists();
	}
	
	/**
	 * 判断索引类型是否存在
	 * @param index
	 * @param type
	 * @param client
	 * @return boolean
	 */
	protected static boolean isIndexTypeExists(Client client, String index, String type) {
		if (Objects.equals(client, null)) {
			logger.info("请求索引客户端为null");
			return false;
		}
		if (StringUtils.isBlank(index)) {
			logger.info("索引为空");
			return false;
		}
		IndicesAdminClient indicesAdminClient = client.admin().indices();
		TypesExistsResponse response = indicesAdminClient.prepareTypesExists(index).setTypes(type).get();
		return response.isExists();
	}
	
	/**
	 * 创建空索引 默认setting 无mapping
	 * @param client
	 * @param index
	 * @return
	 */
	public static boolean createIndex(Client client, String index) {
		if(!isIndexExists(client, index)) {
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			CreateIndexResponse response = indicesAdminClient.prepareCreate(index).get();
			return response.isAcknowledged();
		}else {
			return false;
		}
	}
	
	/**
	 * 创建索引 带setting
	 * 
	 * @param client
	 * @param index
	 * @param settings
	 * @return
	 */
	public static boolean createIndex(Client client, String index, Map<String, ?> settings){
		if(!isIndexExists(client, index)) {
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			CreateIndexResponse response = indicesAdminClient.prepareCreate(index).setSettings(settings).get();
			return response.isAcknowledged();
		}else {
			return false;
		}
	}
	
	/**
	 * 创建索引 带索引设置和索引类型映射
	 * @param client
	 * @param index
	 * @param type
	 * @param indexSettings
	 * @param mappingSettings
	 * @return
	 * @throws ElasticsearchException
	 */
	public static boolean putIndexMapping(Client client, String index, String type, Map<String, ?> indexSettings, String mappingSettings) throws ElasticsearchException{
		try {
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			//当索引存在时允许增加新的索引类型
			if(isIndexExists(client, index)) {
				PutMappingResponse response = indicesAdminClient.preparePutMapping(index)
		        .setType(type)                            
		        .setSource(mappingSettings)
		        .get();
				return response.isAcknowledged();
			}else {
				//创建索引不存在时创建索引并为索引类型指定映射和分片数量
				CreateIndexResponse response = indicesAdminClient.prepareCreate(index)
						.setSettings(indexSettings)
						.addMapping(type, mappingSettings)
						.get();
				return response.isAcknowledged();
			}
		} catch (Exception e) {
			logger.debug("创建索引映射失败", e);
			throw new ElasticsearchException("创建索引映射失败", e);
		} 
	}
	
	/**
	 * 索引单条文档
	 * @param client
	 * @param index
	 * @param type
	 * @param id
	 * @param document
	 * @return
	 * @throws SearchException
	 */
	public static boolean indexDocument(Client client, String index, String type, String id, Map<String, ?> document) throws SearchException{
		IndexResponse response = client.prepareIndex(index, type, id)//构建索引请求
				.setSource(document).get();//索引文档
		return response.isCreated();
	}
	
	/**
	 * 批量索引文档
	 * @param client
	 * @param index
	 * @param type
	 * @param documents 索引文档内容
	 * @return
	 * @throws ElasticsearchException
	 */
	public static boolean indexDocument(Client client, String index, String type, List<Map<String, ?>> documents) throws ElasticsearchException {
		//批量索引请求构建
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Map<String, ?> document : documents) {
			String id = null;
			//判断索引文档列表中，每条文档是否有id,若没有，则ES自动生成一个标识
			if (document.containsKey(ES_ID)) {
				id = String.valueOf(document.get(ES_ID));
			}
			bulkRequest.add(client.prepareIndex(index, type, id).setSource(document));
		}
		//发送批量构建索引请求
		BulkResponse bulkResponse = bulkRequest.get();
		if (bulkResponse.hasFailures()) {
			for (Iterator<BulkItemResponse> iterator = bulkResponse.iterator(); iterator.hasNext();) {
				BulkItemResponse response = iterator.next();
				logger.info("批量索引文档失败 {}, 文档条目ID {}", response.getFailureMessage(), response.getItemId());
			}
			return false;
		} else {
			logger.debug("批量索引文档成功 {} 条", documents.size());
		}
		return true;
	}
	
	/**
	 * 删除索引
	 * @param client
	 * @param index
	 * @return
	 */
	public static boolean deleteIndex(Client client, String index) {
		if(isIndexExists(client, index)){
			IndicesAdminClient indicesAdminClient = client.admin().indices();
			DeleteIndexResponse response = indicesAdminClient.prepareDelete(index).execute().actionGet();
			return response.isAcknowledged();
		}else{
			logger.debug("删除索引 不存在 索引名称：{}", index);
			return false;
		}
	}
	
	public static void deleteByQuery(Client client, String index, String type) {
		StringBuilder b = new StringBuilder();
		b.append("{\"query\":{\"match_all\":{}}}");
		DeleteByQueryRequestBuilder response = new DeleteByQueryRequestBuilder(
				client, DeleteByQueryAction.INSTANCE);
		response.setIndices(index).setTypes(type).setSource(b.toString())
				.execute().actionGet();
	}
	
	/**
	 * 根据条件进行查询
	 * @param client
	 * @param queryParam
	 * @param pageNow
	 * @param pageSize
	 * @return
	 * @throws ElasticsearchException
	 */
	public static ListResult<Map<String, ?>> searcher(Client client, QueryParam queryParam, int startRow, int pageSize) throws ElasticsearchException{
		ListResult<Map<String, ?>> resultList = null;
		long totalCount = 0;
		String keywords = null;
		try {
			String[] indices = queryParam.getIndices().split(",");
			String[] types = queryParam.getTypes().split(",");
			keywords = queryParam.getKeywords();
			String[] queryFields = queryParam.getQueryFields().split(",");
			String[] hightlighterFields = queryParam.getHightlighterFields().split(",");
			String preTags = queryParam.getHighlighterPreTags();
			String postTags = queryParam.getHighlighterPostTags();
			BoolQueryBuilder boolQuery = requireQueryBuilder(queryParam.getConditionMaps());
			QueryStringQueryBuilder queryBuilder = setQueryStringBuilder(keywords, queryFields);
			boolQuery.must(queryBuilder);
			//设置查询构建器
			SearchRequestBuilder searchRequestBuilder = client
					.prepareSearch(indices)
					.setTypes(types)
					.setQuery(boolQuery);
			setHighlighter(hightlighterFields, preTags, postTags, searchRequestBuilder);
			//发送查询请求
			SearchResponse searchResponse = searchRequestBuilder
					.setFrom(startRow)
					.setSize(pageSize)
					.execute()
					.actionGet();
			//获取命中条数
			SearchHits hits = searchResponse.getHits();
			totalCount = hits.getTotalHits();
			logger.info("查询到记录数: {}", totalCount);
			SearchHit[] searchHists = hits.getHits();
			if(searchHists.length > 0){
				// 结果容器
				List<Map<String, ?>> list = new ArrayList<>();
				for (SearchHit hit : searchHists) {
					Map<String, Object> sourceContent = hit.getSource();
					setReturnHighlightField(list, hit, sourceContent);
				}
				resultList = setListResult(pageSize, totalCount, list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索失败 {}", keywords);
			throw e;
		} 
		return resultList;
	}
	
	protected static BoolQueryBuilder requireQueryBuilder(Map<String, String> conditionMaps) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		if(null != conditionMaps) {
			for (Map.Entry<String, String> entry : conditionMaps.entrySet()) {
				String field = entry.getKey();
				String value = entry.getValue();
				TermQueryBuilder termQuery = QueryBuilders.termQuery(field, value);
				boolQuery.must(termQuery);
			}
		}
		return boolQuery;
	}
	
	/**
	 * 用高亮字段值替换原内容
	 * @param list
	 * @param hit
	 * @param sourceContent
	 */
	private static void setReturnHighlightField(List<Map<String, ?>> list, SearchHit hit, Map<String, Object> sourceContent) {
		// 获取对应的高亮域
		Map<String, HighlightField> result = hit.highlightFields();
		for (Map.Entry<String, HighlightField> entry : result.entrySet()) {
			String key = entry.getKey();
			Text[] fragments = entry.getValue().fragments();
			sourceContent.put(StringUtil.removeEndIgnoreCase(key, IK.getSuffix()), StringUtils.join(fragments, ""));
		}
		list.add(sourceContent);
	}
	
	/**
	 * 设置返回结果信息
	 * @param pageSize
	 * @param totalCount
	 * @param list
	 * @return
	 */
	private static ListResult<Map<String, ?>> setListResult(int pageSize, long totalCount, List<Map<String, ?>> list) {
		ListResult<Map<String, ?>> resultList = new ListResult<>();
		resultList.setList(list);
		resultList.setCount(totalCount);
		resultList.setPageSize(pageSize);
		return resultList;
	}
	
	/**
	 * 根据条件进行查询（可以指定返回某几个字段）
	 * @param client
	 * @param queryParam
	 * @param pageNow
	 * @param pageSize
	 * @return
	 * @throws ElasticsearchException
	 */
	public static ListResult<Map<String, ?>> searchHitField(Client client, QueryParam queryParam, int startRow, int pageSize) throws ElasticsearchException{
		ListResult<Map<String, ?>> resultList = null;
		long totalCount = 0;
		String keywords = null;
		try {
			String[] indices = queryParam.getIndices().split(",");
			String[] types = queryParam.getTypes().split(",");
			keywords = queryParam.getKeywords();
			String[] queryFields = queryParam.getQueryFields().split(",");
			String[] hightlighterFields = queryParam.getHightlighterFields().split(",");
			String preTags = queryParam.getHighlighterPreTags();
			String postTags = queryParam.getHighlighterPostTags();
			String[] returnFields = queryParam.getReturnFields().split(",");
			
			// 结果容器
			List<Map<String, ?>> list = null;
			
			QueryStringQueryBuilder queryBuilder = setQueryStringBuilder(keywords, queryFields);
			//设置查询构建器
			SearchRequestBuilder searchRequestBuilder = client
					.prepareSearch(indices)
					.setTypes(types)
					.setQuery(queryBuilder);
			setHighlighter(hightlighterFields, preTags, postTags, searchRequestBuilder);
			//发送查询请求
			SearchResponse searchResponse = searchRequestBuilder
					.setFrom(startRow)
					.setSize(pageSize)
					.addFields(returnFields)
					.execute()
					.actionGet();
			//获取命中条数
			SearchHits hits = searchResponse.getHits();
			totalCount = hits.getTotalHits();
			logger.info("查询到记录数: {}", totalCount);
			SearchHit[] searchHists = hits.getHits();
			if(searchHists.length > 0){
				list = new ArrayList<>();
				for (SearchHit hit : searchHists) {
					Map<String, SearchHitField> fieldMap = hit.getFields();
					setReturnHighlightField(list, hit, setSourceContent(fieldMap));
				}
				resultList = setListResult(pageSize, totalCount, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索失败 {}", keywords);
			throw e;
		} 
		return resultList;
	}

	/**
	 * 设置高亮信息
	 * @param hightlighterFields 高亮字段
	 * @param preTags 前缀标签
	 * @param postTags 后缀标签
	 * @param searchRequestBuilder
	 */
	private static void setHighlighter(String[] hightlighterFields, String preTags, String postTags, SearchRequestBuilder searchRequestBuilder) {
		//设置高亮域
		for (String field : hightlighterFields) {
			searchRequestBuilder.addHighlightedField(field);
		}
		//设置高亮标签
		searchRequestBuilder.setHighlighterPreTags(preTags).setHighlighterPostTags(postTags);
	}
	
	/**
	 * 设置查询字符串构建器
	 * @param keywords
	 * @param queryFields
	 * @return QueryStringQueryBuilder
	 */
	private static QueryStringQueryBuilder setQueryStringBuilder(String keywords, String[] queryFields) {
		QueryStringQueryBuilder  queryBuilder = QueryBuilders.queryStringQuery(keywords);
		//设置多个查询域
		for (String field : queryFields) {
			queryBuilder.field(field);
		}
		return queryBuilder;
	}
	
	/**
	 * 查询所有记录
	 * @param client
	 * @param queryParam
	 * @param startRow
	 * @param pageSize
	 * @return List<Map<String,Object>>
	 * @throws ElasticsearchException
	 */
	public static List<Map<String, Object>> searchAll(Client client, QueryParam queryParam, int startRow, int pageSize) throws ElasticsearchException{
		// 结果容器
		List<Map<String, Object>> resultList = new ArrayList<>();
		try {
			String[] indices = queryParam.getIndices().split(",");
			String[] types = queryParam.getTypes().split(",");
			String[] returnFields = queryParam.getReturnFields().split(",");
			
			MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
			//设置查询构建器
			SearchRequestBuilder searchRequestBuilder = client
					.prepareSearch(indices)
					.setTypes(types)
					.setQuery(matchAllQuery);
			//发送查询请求
			SearchResponse searchResponse = searchRequestBuilder
					.setFrom(startRow)
					.setSize(pageSize)
					.addFields(returnFields)
					.execute()
					.actionGet();
			//获取命中条数
			SearchHits hits = searchResponse.getHits();
			SearchHit[] searchHists = hits.getHits();
			logger.debug("查询到记录数: {}", hits.getTotalHits());
			if(searchHists.length > 0){
				resultList = new ArrayList<>();
				for (SearchHit hit : searchHists) {
					Map<String, SearchHitField> fieldMap = hit.getFields();
					resultList.add(setSourceContent(fieldMap));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("搜索失败 {}", e);
			throw e;
		} 
		return resultList;
	}

	private static Map<String, Object> setSourceContent(Map<String, SearchHitField> fieldMap) {
		Map<String, Object> sourceContent = new HashMap<>();
		for (Entry<String, SearchHitField> entry : fieldMap.entrySet()) {
			SearchHitField searchHitField = entry.getValue();
			sourceContent.put(searchHitField.getName(), searchHitField.getValue());
		}
		return sourceContent;
	}
}
