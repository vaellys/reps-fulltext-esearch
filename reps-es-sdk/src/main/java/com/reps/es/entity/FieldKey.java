package com.reps.es.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.reps.es.entity.enums.FieldType;
import com.reps.es.util.AnalyzerEnum;

public class FieldKey {
	
	/** 是否存储 */
	private boolean store = true;
	
	private String type = "string";
	
	private String analyzer;
	
	private String index;
	
	private String term_vector;
	
	private FieldKey fields;

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

	public FieldKey getFields() {
		return fields;
	}

	public void setFields(FieldKey fields) {
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
		FieldKey key = new FieldKey();
		key.setStore(true);
		key.setType(FieldType.STRING.getValue());
		FieldKey fields = new FieldKey();
		fields.setAnalyzer(AnalyzerEnum.IK.getAnalyzer());
		key.setFields(fields);
		keyMap.put("name", key);
		
		String jsonString = JSON.toJSONString(keyMap);
		System.out.println(jsonString);
	}
	
}
