package com.reps.es.entity;

import java.io.Serializable;

/**
 * es请求参数配置
 * @author qianguobing
 * @date 2017年10月17日 下午3:15:52
 */
public class ConfigParam implements Serializable{
	
	private static final long serialVersionUID = -6012265652601474802L;

	/** es 服务IP */
	private String ip;
	
	/** es 服务端口 默认9300 */
	private Integer port;
	
	/** es索引名称 */
	private String index;
	
	/** es索引类型 */
	private String type;
	
	public ConfigParam() {
		
	}
	
	public ConfigParam(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ConfigParam [ip=" + ip + ", port=" + port + ", index=" + index + ", type=" + type + "]";
	}
	
}
