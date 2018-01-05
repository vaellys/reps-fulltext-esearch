package com.reps.es.rest;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.restful.RestBaseController;
import com.reps.core.restful.RestResponse;
import com.reps.core.restful.RestResponseStatus;
import com.reps.es.bean.ContentVo;
import com.reps.es.sdk.service.IESearchIndexProvider;


@RestController
@RequestMapping(value = "/uapi/es")
public class ESearchServiceURest extends RestBaseController {

	private static Log logger = LogFactory.getLog(ESearchServiceURest.class);

	@Autowired(required = false)
	private IESearchIndexProvider provider;

	@RequestMapping(value = "/addindex", method = { RequestMethod.POST })
	public RestResponse<String> add(@RequestParam(name = "contents", required = true) String contents) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			provider.addIndex(Arrays.asList(mapper.readValue(contents, ContentVo[].class)));
		} catch (Exception e) {
			logger.error("创建索引失败!", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "创建索引失败：" + e.getMessage());
		}
		return wrap(RestResponseStatus.OK, "创建索引成功！");
	}
}
