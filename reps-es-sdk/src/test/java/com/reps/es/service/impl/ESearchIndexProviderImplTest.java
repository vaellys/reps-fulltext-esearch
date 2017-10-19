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

import com.reps.es.entity.ConfigParam;
import com.reps.es.entity.FieldKey;
import com.reps.es.entity.enums.FieldType;
import com.reps.es.entity.enums.IndexType;
import com.reps.es.service.IESearchIndexProvider;
import com.reps.es.util.AnalyzerEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/reps-es.xml" })
public class ESearchIndexProviderImplTest {
	
	 /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(ESearchIndexProviderImplTest.class);
    
    @Autowired
    private IESearchIndexProvider esService;
    
    @Test
    public void testAddIndex() {
    	try {
    		//索引数据
            List<Map<String, ?>> documents = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("id", "10");
            map.put("title", "SB");
            map.put("name", "王六");
            documents.add(map);
            logger.debug("data list map {}", documents);
            //参数配置
			ConfigParam configParam = new ConfigParam();
			configParam.setIndex("reps");
			configParam.setType("organize");
			configParam.setIp("localhost");
			configParam.setPort(9300);
			//键映射
			Map<String, FieldKey> keyMap = new HashMap<>();
			FieldKey key = new FieldKey();
			key.setStore(true);
			key.setType(FieldType.STRING.getValue());
			key.setAnalyzer(AnalyzerEnum.IK.getAnalyzer());
			keyMap.put("name", key);
			FieldKey idKey = new FieldKey();
			idKey.setIndex(IndexType.NOT_ANALYZED.getValue());
			keyMap.put("id", idKey);
			boolean result = esService.addIndex(configParam, keyMap, documents);
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
    
    @Test
    public void testDelete() {
    	try {
    		//删除数据
            List<String> ids = new ArrayList<>();
            ids.add("AV8pkBL9U8rKE5DcEov3");
            logger.debug("data list map {}", ids);
            //参数配置
			ConfigParam configParam = new ConfigParam();
			configParam.setIndex("reps");
			configParam.setType("organize");
			configParam.setIp("localhost");
			configParam.setPort(9300);
			boolean result = esService.delete(configParam, ids);
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------删除索引失败----------");
        }
    }

}
