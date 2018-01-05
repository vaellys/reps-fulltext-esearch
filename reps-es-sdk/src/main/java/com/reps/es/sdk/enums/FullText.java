package com.reps.es.sdk.enums;

public enum FullText {

	INDEX("索引名称", "index"), TYPE("索引类型", "content");

	private String code;
	private String text;

	private FullText(String text, String code) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
