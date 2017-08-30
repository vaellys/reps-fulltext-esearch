package com.reps.es.util;

import java.net.InetSocketAddress;
import java.util.HashMap;
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
			logger.debug("集群配置信息 {}", esConfig);
			client = TransportClient.builder().addPlugin(ShieldPlugin.class).addPlugin(DeleteByQueryPlugin.class).settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(this.host, this.port)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取集群客户端 client 失败", e);
		}
		return client;
	}
	
	public static void main(String[] args) {
		String host = "localhost";
		Map<String, String> esConfig = new HashMap<>();
		esConfig.put("cluster.name", "elastic");
		esConfig.put("shield.user", "es_admin:vaellys");
		esConfig.put("client.transport.sniff", "true");
		esConfig.put("client.transport.ping_timeout", "20s");
		esConfig.put("client.transport.nodes_sampler_interval", "5s");
		esConfig.put("client.transport.ignore_cluster_name", "false");
		ElasticsearchManager esManager = new ElasticsearchManager(host, 9300);
		Client client = esManager.getClient();
		System.out.println(client);
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
