package com.reps.es.entity.enums;

public enum FieldType {
	
	INT("日期", "int"), STRING("字符串", "string"), DATE("日期", "date");
	
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
