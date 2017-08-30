package com.reps.es.action;

import static com.reps.es.util.LoadResourcesUtil.getResources;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reps.core.commons.Pagination;
import com.reps.core.orm.ListResult;
import com.reps.core.restful.RestBaseController;
import com.reps.core.restful.RestResponse;
import com.reps.core.restful.RestResponseStatus;
import com.reps.core.util.StringUtil;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.util.IndicesManager;
import com.reps.es.util.QueryParam;

@Controller
@RequestMapping(value = "/es")
public class SearchAction extends RestBaseController{

	private static Log logger = LogFactory.getLog(SearchAction.class);

	@Autowired
	private IElasticsearchService elasticsearchService;

	@Autowired
	private IndicesManager indicesManager;
	
	@Autowired
	private QueryParam queryParam;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addIndex", method = { RequestMethod.POST })
	@ResponseBody
	public RestResponse<String> addIndex(
	// 索引名称
			@RequestParam(name = "index", required = true) String index,
			// 索引类型
			@RequestParam(name = "type", required = true) String type,
			// 索引内容
			@RequestParam(name = "documents", required = true) String documents) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			elasticsearchService.addIndex(index, type,
					mapper.readValue(documents, List.class));
		} catch (Exception e) {
			logger.error("创建索引失败!", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "创建索引失败：" + e.getMessage());
		}
		return wrap(RestResponseStatus.OK, "创建索引成功！");
	}

	@RequestMapping(value = "/addMapping", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public RestResponse<String> addIndexMapping(
			// 索引名称
			@RequestParam(name = "index", required = true) String index,
			// 索引类型
			@RequestParam(name = "type", required = false) String type) {
		try {
			elasticsearchService.addIndexMapping(index, type,
					indicesManager.getIndexSetting(), getResources());
		} catch (Exception e) {
			logger.error("创建索引映射失败!", e);
			 return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "创建索引映射失败：" +
			 e.getMessage());
		}
		return wrap(RestResponseStatus.OK, "创建索引成功！");
	}
	
	/**
	 * 查询接口
	* @author liuerlong
	* @date  2017年7月24日 下午4:57:03
	* @param index 索引名称
	* @param type 索引类型(organize(机构))
	* @param pager 分页对象
	* @param keywords 关键词
	* @return
	* @return RestResponse<ListResult<Map<String,?>>>
	 */
	@RequestMapping(value = "/search", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public RestResponse<ListResult<Map<String, ?>>> search(
			@RequestParam(name = "index", required = true) String index,
			@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "pageIndex", required = false) Integer pageIndex,
			@RequestParam(name = "pageSize", required = false) Integer pageSize,
			@RequestParam(name = "keywords", required = true) String keywords) {
		ListResult<Map<String, ?>> result = null;
		try {
			Pagination pager = setQueryParam(index, type, pageIndex, pageSize, keywords);
			
			result = elasticsearchService.search(queryParam, pager.getStartRow(), pager.getPageSize());
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
	
	@RequestMapping(value = "/specialSearch", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public RestResponse<ListResult<Map<String, ?>>> specialSearch(
			@RequestParam(name = "index", required = true) String index,
			@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "pageIndex", required = false) Integer pageIndex,
			@RequestParam(name = "pageSize", required = false) Integer pageSize,
			@RequestParam(name = "keywords", required = true) String keywords) {
		ListResult<Map<String, ?>> result = null;
		try {
			Pagination pager = setQueryParam(index, type, pageIndex, pageSize, keywords);
			result = elasticsearchService.specialSearch(queryParam, pager.getStartRow(), pager.getPageSize());
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

	private Pagination setQueryParam(String index, String type, Integer pageIndex, Integer pageSize, String keywords) {
		Pagination pager = new Pagination();
		pager.setCurPageNumber(pageIndex == null ? 1 : pageIndex);
		if(StringUtil.isNotBlank(index)){
			queryParam.setIndices(index);
		}
		if(StringUtil.isNotBlank(type)){
			queryParam.setTypes(type);
		}
		if(null != keywords) {
			queryParam.setKeywords(keywords);
			pager.setPageSize(pageSize == null ? 20 : pageSize);
		}else {
			pager.setPageSize(pageSize == null ? 5000 : pageSize);
		}
		return pager;
	}
	
	@RequestMapping(value = "/searchAll", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Object searchAll(
			@RequestParam(name = "index", required = true) String index,
			@RequestParam(name = "type", required = true) String type,
			@RequestParam(name = "pageIndex", required = false) Integer pageIndex,
			@RequestParam(name = "pageSize", required = false) Integer pageSize) {
		List<Map<String, Object>> result = null;
		try {
			Pagination pager = setQueryParam(index, type, pageIndex, pageSize, null);
			result = elasticsearchService.searchAll(queryParam, pager.getStartRow(), pager.getPageSize());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("全文检索失败!", e);
			return wrap(RestResponseStatus.INTERNAL_SERVER_ERROR, "全文检索失败：" + e.getMessage());
		}
		return result;
	}
}
