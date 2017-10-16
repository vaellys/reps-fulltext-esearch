package com.reps.es.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.reps.es.service.IElasticsearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/reps-es.xml" })
public class ElasticsearchServiceImplTest {
	
	 /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImplTest.class);
    
    @Autowired
    private IElasticsearchService elasticsearchService;
    
    @Test
    public void testAddIndex() {
    	try {
            List<Map<String, ?>> documents = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("id", "1");
            map.put("title", "你好");
            map.put("content", "中华人民共和国");
            documents.add(map);
            logger.debug("data list map {}", documents);
			boolean result = elasticsearchService.addIndex("reps", "organize", documents);
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }

}
