package com.reps.es.rest;

import static com.reps.es.util.PageUtil.cps;
import static com.reps.es.util.PageUtil.getCurrentNum;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reps.core.orm.ListResult;
import com.reps.core.restful.RestBaseController;
import com.reps.core.restful.RestResponse;
import com.reps.core.restful.RestResponseStatus;
import com.reps.es.sdk.service.IESearchIndexProvider;


@RestController
@RequestMapping(value = "/oapi/es")
public class ESearchServiceORest extends RestBaseController {

	private static Log logger = LogFactory.getLog(ESearchServiceORest.class);

	@Autowired(required = false)
	private IESearchIndexProvider provider;

	/**
	 * 全文检索 （通用）
	 * 搜索内容为content
	 * @param keywords 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	public RestResponse<ListResult<Map<String, ?>>> search(
			@RequestParam(name = "keywords", required = true) String keywords,
			@RequestParam(name = "pageIndex", required = false) Integer pageIndex,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		try {
			ListResult<Map<String, ?>> result = provider.search(keywords, getCurrentNum(pageIndex, pageSize), cps(pageSize));
			if (result == null || result.getCount() == 0 ) {
				return wrap(RestResponseStatus.NOT_FOUND, "全文检索查询记录为空");
			}
			return wrap(RestResponseStatus.OK, "全文检索查询成功", result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("全文检索失败!", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "全文检索失败：" + e.getMessage());
		}
	}
}
