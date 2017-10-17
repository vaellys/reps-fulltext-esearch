package com.reps.es.entity;

import java.io.Serializable;

public class ConfigParam implements Serializable{
	
	private static final long serialVersionUID = -6012265652601474802L;

	private String ip;
	
	private Integer port;
	
	private String index;
	
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
	
}
