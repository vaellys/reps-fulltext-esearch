package com.reps.es.sdk.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
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

import com.alibaba.fastjson.JSON;
import com.reps.core.orm.ListResult;
import com.reps.core.util.DateUtil;
import com.reps.es.bean.ContentVo;
import com.reps.es.sdk.config.FieldKey;
import com.reps.es.sdk.config.ProviderConfig;
import com.reps.es.sdk.enums.FieldType;
import com.reps.es.sdk.enums.IndexType;
import com.reps.es.sdk.service.IESearchIndexProvider;

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
            map.put("id", "5");
            map.put("name", "张飞");
            documents.add(map);
            logger.debug("data list map {}", documents);
            //参数配置
			ProviderConfig configParam = new ProviderConfig();
			configParam.setIndex("reps");
			configParam.setType("organize");
			configParam.setIp("localhost");
			configParam.setPort(9300);
			//键映射
			Map<String, FieldKey> keyMap = new HashMap<>();
			keyMap.put("name", new FieldKey());
			FieldKey idKey = new FieldKey(true, FieldType.STRING, IndexType.NO);
			keyMap.put("id", idKey);
			boolean result = esService.addIndex(configParam, keyMap, documents);
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
    
    @Test
    public void testAddIndex2() {
    	try {
    		//索引数据
            List<ContentVo> documents = new ArrayList<>();
            ContentVo content = new ContentVo("中国自主研发航母", "中国自主研发航母", "https://www.cnblogs.com/crows/p/4702975.html", new Date());
            documents.add(content);
            logger.debug("data list map {}", documents);
			boolean result = esService.addIndex(documents);
            assertEquals(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
    
    @Test
    public void testAddIndex3() {
    	try {
    		//索引数据
            List<Map<String, ?>> documents = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("addTime", DateUtil.getDateFromStr(DateUtil.getCurDateTime("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
            documents.add(map);
            logger.debug("data list map {}", documents);
            //参数配置
			ProviderConfig configParam = new ProviderConfig();
			configParam.setIndex("res");
			configParam.setType("res");
			configParam.setIp("localhost");
			configParam.setPort(9300);
			//键映射
			Map<String, FieldKey> keyMap = new HashMap<>();
			FieldKey idKey = new FieldKey(true, FieldType.DATE, IndexType.NO);
			keyMap.put("addTime", idKey);
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
			ProviderConfig configParam = new ProviderConfig();
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
    
    @Test
    public void testSearch() {
    	try {
    		String keywords = "刘";
    		//查询字段列表
            String[] fields = new String[] {"name", "resSummary", "resContent"};
            logger.debug("query data field list  {}", JSON.toJSONString(fields));
            //参数配置
			ProviderConfig configParam = new ProviderConfig();
			configParam.setIndex("reps");
			configParam.setType("organize");
			configParam.setIp("localhost");
			configParam.setPort(9300);
			ListResult<Map<String, ?>> searchResult = esService.search(configParam, fields, keywords, 0, 28);
			logger.debug("search result\n {}", JSON.toJSONString(searchResult));
            assertNotNull(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }

}
