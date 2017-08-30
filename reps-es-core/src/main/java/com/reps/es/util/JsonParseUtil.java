package com.reps.es.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParseUtil {
	
	/**
	 * {"status":200,"message":"服务调用成功","result":
	 * {"count":22,"pageSize":22,
	 * "list":
	 * [
	 * {"id":"40289ec2533f2cd301533f2ce014000b","name":"儿童教育学校","code":null,"description":null,"logoUrl":null,"district":"640000","orgType":"11","parentId":"64000020151231org000000000000000"}
	 * ]
	 * ,"totalPages":1}}
	 * @param responseJson
	 * @return
	 */
	
	private static Logger logger = LoggerFactory.getLogger(JsonParseUtil.class);
	
	public static final String STATUS = "status";
	
	public static final String RESULT = "result";
	
	public static final String LIST = "list";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, ?>> parse(String responseJson){
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map message = mapper.readValue(responseJson, Map.class);
			if(200 == (int)message.get(STATUS)){
				Map<String, ?> result = (Map<String, ?>) message.get(RESULT);
				List<Map<String, ?>> datas = (List) result.get(LIST);
				return datas;
			}else{
				logger.debug("服务调用不成功 {}", responseJson);
			}
		} catch (JsonParseException e) {
			logger.error("json解析异常");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			logger.error("json映射异常");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("解析数据失败");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String responseJson = "{\"status\":200, \"result\":{\"list\":[{}]}}";
		List<Map<String, ?>> parse = parse(responseJson);
		System.out.println(parse);
	}
}
