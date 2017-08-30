package com.reps.es.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class LoadResourcesUtil {

	private static Log logger = LogFactory.getLog(LoadResourcesUtil.class);

	private static final String FILE_NAME_SUFFIX = ".json";

	public static Map<String, String> getResources() {
		// key为映射类型即文件名字，value为文件内容
		Map<String, String> mappingList = new LinkedHashMap<>();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resourcePatternResolver
					.getResources("classpath:**/mapping/*.json");
			for (Resource resource : resources) {
				mappingList.put(StringUtils.remove(resource.getFilename(),
						FILE_NAME_SUFFIX), IOUtils.toString(
						resource.getInputStream(), "utf-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("映射资源获取失败", e);
		}
		return mappingList;
	}

	public static void main(String[] args) {
		Map<String, String> resources = getResources();
		System.out.println(resources);
	}
}
