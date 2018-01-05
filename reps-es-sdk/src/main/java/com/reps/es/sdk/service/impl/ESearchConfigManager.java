package com.reps.es.sdk.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

import com.reps.es.util.ElasticsearchManager;

@Component("esManager")
public class ESearchConfigManager extends ElasticsearchManager{
	
	private Client client;
	
	public ESearchConfigManager() {
		super("localhost", 9300);
		Map<String, String> esConfig = new HashMap<>();
		esConfig.put("cluster.name", "elastic");
		esConfig.put("shield.user", "es_admin:vaellys");
		esConfig.put("client.transport.sniff", "false");
		esConfig.put("client.transport.ping_timeout", "20s");
		esConfig.put("client.transport.nodes_sampler_interval", "5s");
		esConfig.put("client.transport.ignore_cluster_name", "false");
		this.setEsConfig(esConfig);
	}

	public Client getClient() {
		if(null == client) {
			this.client = super.getClient();
		}
		return client;
	}
	
	@PreDestroy
	public void close() {
		if(null != client) {
			client.close();
		}
	}

}
