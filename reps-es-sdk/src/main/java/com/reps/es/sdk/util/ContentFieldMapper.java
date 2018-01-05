package com.reps.es.sdk.util;

import java.util.HashMap;
import java.util.Map;

import com.reps.es.exception.ElasticsearchException;
import com.reps.es.sdk.config.FieldKey;
import com.reps.es.sdk.enums.FieldType;
import com.reps.es.sdk.enums.IndexType;

/**
 * 字段映射器设置 针对于自己定义的内部实体
 * 
 * @author qianguobing
 * @date 2018年1月3日 下午3:55:03
 */
public class ContentFieldMapper {
	
	/**
	 * 内容映射字段常量 
	 */
	public static final String FIELD_MAPPER_TITLE = "title";
	
	public static final String FIELD_MAPPER_SUMMARY = "summary";
	
	public static final String FIELD_MAPPER_URL = "url";
	
	public static final String FIELD_MAPPER_TIME = "time";

	private ContentFieldMapper() {
	}

	/**
	 * 添加字段映射
	 * @param keyMapper
	 * @param key
	 * @param fieldKey
	 * @return Map<String, FieldKey>
	 */
	public static Map<String, FieldKey> addKeyMapper(Map<String, FieldKey> keyMapper, String key, FieldKey fieldKey) {
		keyMapper.put(key, fieldKey);
		return keyMapper;
	}

	/**
	 * 构建全文检索字段映射器
	 * @return Map<String, FieldKey>
	 * @throws ElasticsearchException
	 */
	public static Map<String, FieldKey> buildKeyMapper() throws ElasticsearchException {
		Map<String, FieldKey> keyMapper = new HashMap<>();
		keyMapper.put(FIELD_MAPPER_TITLE, new FieldKey());
		keyMapper.put(FIELD_MAPPER_SUMMARY, new FieldKey());
		keyMapper.put(FIELD_MAPPER_URL, new FieldKey(true, FieldType.STRING, IndexType.NO));
		keyMapper.put(FIELD_MAPPER_TIME, new FieldKey(true, FieldType.DATE));
		return keyMapper;
	}

}
