package com.reps.es.util;

/**
 * 分词器
 * @author qianguobing
 * @date 2017年8月30日 上午10:17:03
 */
public enum AnalyzerEnum {
	IK("ik", ".ik");
	
	private String analyzer;
	
	private String suffix;
	
	AnalyzerEnum(String analyzer, String suffix){
		this.analyzer = analyzer;
		this.suffix = suffix;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
