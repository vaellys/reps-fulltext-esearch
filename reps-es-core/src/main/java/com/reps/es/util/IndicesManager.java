package com.reps.es.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class IndicesManager {
	
	/**
	 * 索引设置（主分片，副本分片）
	 */
	private Map<String, ?> indexSetting = new LinkedHashMap<>();
	
	public Map<String, ?> getIndexSetting() {
		return indexSetting;
	}
	public void setIndexSetting(Map<String, ?> indexSetting) {
		this.indexSetting = indexSetting;
	}
}
