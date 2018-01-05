package com.reps.es.util;

import java.util.Map;

/**
 * @ClassName: QueryParamDTO
 * @Description: 查询参数bean
 * @author: qianguobing
 * @date: 2017年7月23日 下午1:30:05
 * @version: V1.0
 */
public class QueryParam {
	
	/**
	 * 索引名称,多个以逗号隔开
	 */
	private String indices;
	
	/**
	 * 索引类型,多个以逗号隔开
	 */
	private String types;
	
	/**
	 * 查询关键词
	 */
	private String keywords = "";
	
	/**
	 * 查询字段域,多个以逗号隔开
	 */
	private String queryFields;
	
	/**
	 * 高亮显示字段,多个以逗号隔开
	 */
	private String hightlighterFields = "";
	
	/**
	 * 高亮前缀标签
	 */
	private String highlighterPreTags = "";
	
	/**
	 * 高亮后缀标签
	 */
	private String highlighterPostTags = "";
	
	/**
	 * 返回指定字段
	 */
	private String returnFields;
	
	/**
	 * 条件map
	 */
	private Map<String, String> conditionMaps;
	
	public QueryParam() {
	}

	public String getIndices() {
		return indices;
	}

	public void setIndices(String indices) {
		this.indices = indices;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(String queryFields) {
		this.queryFields = queryFields;
	}

	public String getHightlighterFields() {
		return hightlighterFields;
	}

	public void setHightlighterFields(String hightlighterFields) {
		this.hightlighterFields = hightlighterFields;
	}

	public String getHighlighterPreTags() {
		return highlighterPreTags;
	}

	public void setHighlighterPreTags(String highlighterPreTags) {
		this.highlighterPreTags = highlighterPreTags;
	}

	public String getHighlighterPostTags() {
		return highlighterPostTags;
	}

	public void setHighlighterPostTags(String highlighterPostTags) {
		this.highlighterPostTags = highlighterPostTags;
	}

	public String getReturnFields() {
		return returnFields;
	}

	public void setReturnFields(String returnFields) {
		this.returnFields = returnFields;
	}

	public Map<String, String> getConditionMaps() {
		return conditionMaps;
	}

	public void setConditionMaps(Map<String, String> conditionMaps) {
		this.conditionMaps = conditionMaps;
	}
	
}
