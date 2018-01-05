package com.reps.es.sdk.config;

import java.util.Map;

import com.reps.es.sdk.enums.FieldType;
import com.reps.es.sdk.enums.IndexType;
import com.reps.es.util.AnalyzerEnum;
/**
 * 索引键值相关配置
 * @param store : 内容是否存储
 * @param type :  映射字段类型
 * @param index : 索引类型 枚举
 * @param analyzer : 分词器名称 枚举
 * @param term_vector : 词向量 用于高亮显示
 * @author liuerlong
 * @date 2017年10月19日 下午3:35:42
 *
 */
public class FieldKey {
	
	/** 映射字段类型 */
	private String type = "string";
	
	/** 内容是否存储 */
	private boolean store = true;
	
	/** 索引类型 通过IndexType 来获取值 */
	private String index;
	
	/** 分词器名称 通过AnalyzerEnum 来获取*/
	private String analyzer;
	
	/** 词向量 用于高亮显示*/
	private String term_vector;
	
	/** 日期格式 */
	private String format;
	
	/** 映射字级字段 暂时不用 */
	private Map<String, FieldKey> fields;
	
	public FieldKey() {
		this.index = IndexType.ANALYZED.getValue();
		this.analyzer = AnalyzerEnum.IK.getAnalyzer();
		this.term_vector = "with_positions_offsets";
	}

	/**
	 * 
	* <p>构造</p>
	* <p>描述说明</p>
	* @author liuerlong
	* @param store : 内容是否存储
	 */
	public FieldKey(boolean store){
		this.store = store;
	}
	
	/**
	 * 
	* <p>构造</p>
	* <p>描述说明</p>
	* @author liuerlong
	* @param store : 内容是否存储
	* type : 映射字段类型
	 */
	public FieldKey(boolean store, FieldType type){
		this.store = store;
		this.type = type.getValue();
		this.checkField(type.getValue());
	}
	
	private void checkField(String fieldType) {
		if(FieldType.DATE.getValue().equals(fieldType)) {
			this.setFormat("yyyy-MM-dd HH:mm:ss");
		}
	}
	
	/**
	 * 
	* <p>构造</p>
	* <p>描述说明</p>
	* @author liuerlong
	* @param store : 内容是否存储
	* @param type : 映射字段类型
	* @param index : 索引类型 枚举
	 */
	public FieldKey(boolean store, FieldType type, IndexType index){
		this.store = store;
		this.type = type.getValue();
		this.index = index.getValue();
		this.checkField(type.getValue());
	}
	
	public FieldKey(boolean store, FieldType type, IndexType index, AnalyzerEnum analyzer){
		this.store = store;
		this.type = type.getValue();
		this.index = index.getValue();
		this.analyzer = analyzer.getAnalyzer();
		this.term_vector  = "with_positions_offsets";
	}
	
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

	public Map<String, FieldKey> getFields() {
		return fields;
	}

	public void setFields(Map<String, FieldKey> fields) {
		this.fields = fields;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
}
