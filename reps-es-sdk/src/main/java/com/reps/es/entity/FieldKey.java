package com.reps.es.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.reps.es.entity.enums.FieldType;
import com.reps.es.util.AnalyzerEnum;

public class FieldKey {
	
	/** 内容是否存储 */
	private boolean store = true;
	
	/** 映射字段类型 */
	private String type = "string";
	
	/** 分词器名称 通过AnalyzerEnum 来获取*/
	private String analyzer;
	
	/** 索引类型 通过IndexType 来获取值 */
	private String index;
	
	/** 词向量 用于高亮显示*/
	private String term_vector;
	
	/** 映射字级字段 暂时不用 */
	private Map<String, FieldKey> fields;

	public boolean isStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getTerm_vector() {
		return term_vector;
	}

	public void setTerm_vector(String term_vector) {
		this.term_vector = term_vector;
	}

	public Map<String, FieldKey> getFields() {
		return fields;
	}

	public void setFields(Map<String, FieldKey> fields) {
		this.fields = fields;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}
	
	public static void main(String[] args) {
		
		Map<String, FieldKey> keyMap = new HashMap<>();
		
		FieldKey fields = new FieldKey();
		fields.setStore(true);
		fields.setType(FieldType.STRING.getValue());
		
		FieldKey key = new FieldKey();
		key.setAnalyzer(AnalyzerEnum.IK.getAnalyzer());
		
		Map<String, FieldKey> fieldKeyMap = new HashMap<>();
		fieldKeyMap.put(AnalyzerEnum.IK.getAnalyzer(), key);
		
		fields.setFields(fieldKeyMap);
		
		keyMap.put("name", fields);
		
		String jsonString = JSON.toJSONString(keyMap);
		System.out.println(jsonString);
	}
	
}
