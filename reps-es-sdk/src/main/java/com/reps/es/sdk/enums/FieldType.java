package com.reps.es.sdk.enums;

/**
 * 字段类型相关配置
 * @param INT : 整型
 * @param STRING :  字符串
 * @param DATE : 日期类型
 * @param DOUBLE : 双精度类型
 * @author liuerlong
 * @date 2017年10月19日 下午3:35:42
 */
public enum FieldType {
	
	INT("整型", "integer"), STRING("字符串", "string"), DATE("日期", "date"), DOUBLE("双精度", "double");
	
	private String name;
	
	private String value;
	
	private FieldType(String name, String value) {
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
