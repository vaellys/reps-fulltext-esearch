package com.reps.es.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.reps.es.exception.ElasticsearchException;
import com.reps.es.service.IRebuildIndexService;

//@Component("beanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger logger = LoggerFactory.getLogger(BeanDefineConfigue.class);
	
	@Autowired
	private IRebuildIndexService rebuildIndexStartTask;
	
	/**
	 * 第一次启动索引初始化
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			rebuildIndexStartTask.rebuildIndex();
		} catch (ElasticsearchException e) {
			logger.error("索引初始化失败", e);
			e.printStackTrace();
		}
	}

}
