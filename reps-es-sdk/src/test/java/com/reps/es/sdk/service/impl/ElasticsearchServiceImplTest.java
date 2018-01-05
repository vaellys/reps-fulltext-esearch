package com.reps.es.sdk.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.reps.es.sdk.service.impl.ESearchConfigManager;
import com.reps.es.service.IElasticsearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/reps-es.xml" })
public class ElasticsearchServiceImplTest {
	
	 /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImplTest.class);
    
    @Autowired
    private IElasticsearchService esService;
    
    @Autowired
    private ESearchConfigManager configManager;
    
    @Test
    public void testDeleteIndex() {
    	try {
            //参数配置
    		configManager.setHost("localhost");
    		configManager.setPort(9300);
    		logger.debug("config manager client {}, host {}, port {}", configManager.getClient(), configManager.getHost(), configManager.getPort());
			boolean result = esService.deleteIndex("index");
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------删除索引失败----------");
        }
    }

}
