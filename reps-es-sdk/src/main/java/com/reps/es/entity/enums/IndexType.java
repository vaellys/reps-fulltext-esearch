package com.reps.es.entity.enums;

public enum IndexType {
	
	NO("不建索引", "no"), NOT_ANALYZED("不分词", "not_analyzed"), ANALYZED("分词", "analyzed");
	
	private String name;
	
	private String value;
	
	private IndexType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
