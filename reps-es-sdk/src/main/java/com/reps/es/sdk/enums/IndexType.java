package com.reps.es.sdk.enums;

/**
 * 索引类型相关配置
 * @param NO : 不建索引且不分词
 * @param NOT_ANALYZED :  不分词
 * @param ANALYZED : 使用分词器进行分词
 * @author liuerlong
 * @date 2017年10月19日 下午3:35:42
 */
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
