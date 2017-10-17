package com.reps.es.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.reps.es.entity.FieldKey;

public class MappingBuilder {
	
	/**
	 * key 为映射字段
	 * value 字段类型
	 * @param mappingConfig
	 * @return String
	 */
	public static String getMappingJsonString(Map<String, FieldKey> mappingConfig) {
		return JSON.toJSONString(mappingConfig);
	}
	
}
