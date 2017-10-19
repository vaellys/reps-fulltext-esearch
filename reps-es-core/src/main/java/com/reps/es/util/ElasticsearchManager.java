package com.reps.es.util;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin;
import org.elasticsearch.shield.ShieldPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchManager {
	
	private static Logger logger = LoggerFactory.getLogger(ElasticsearchManager.class);
	
	/**
	 * es 客户端
	 */
	private Client client = null;
	
	/**
	 * 集群主机地址
	 */
	private String host;
	
	/**
	 * 节点之间通信端口
	 */
	private int port;
	
	private Map<String, String> esConfig = new LinkedHashMap<>();
	
	public ElasticsearchManager(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public void setEsConfig(Map<String, String> esConfig) {
		this.esConfig = esConfig;
	}

	/**
	 * 获取操作es客户端
	 * @return Client
	 */
	public Client getClient() {
		try {
			Settings settings = Settings.settingsBuilder()
			        .put(esConfig)
			        .build();
			logger.info("集群配置信息 {}", esConfig);
			client = TransportClient.builder().addPlugin(ShieldPlugin.class).addPlugin(DeleteByQueryPlugin.class).settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(this.host, this.port)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取集群客户端 client 失败", e);
		}
		return client;
	}
	
	public void close() {
		if(null != client) {
			client.close();
		}
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
