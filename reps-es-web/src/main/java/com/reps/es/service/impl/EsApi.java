package com.reps.es.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.reps.core.api.IApiExecutor;
import com.reps.core.api.PageResult;
import com.reps.core.commons.Pagination;
import com.reps.core.exception.ApiException;
import com.reps.core.orm.ListResult;
import com.reps.core.util.StringUtil;
import com.reps.es.action.SearchAction;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.util.QueryParam;

@Service("es")
public class EsApi implements IApiExecutor{
	
	private static Log logger = LogFactory.getLog(SearchAction.class);
	
	@Autowired
	private IElasticsearchService elasticsearchService;

	@Autowired
	private QueryParam queryParam;

	@Override
    public PageResult outJson(int page, String apiName, Map<String, String> params) throws ApiException {
		ListResult<Map<String, ?>> result = null;
    	try {
			Pagination pager = new Pagination();
			pager.setPageSize(RESULT_SIZE);
			pager.setCurPageNumber(page);
			if("search".equals(apiName)){
				setQueryParams(params, pager, true, false);
				result = elasticsearchService.search(queryParam, pager.getStartRow(), pager.getPageSize());
				return buildReturnResult(result, pager);
			}else if("specialSearch".equals(apiName)){
				setQueryParams(params, pager, true, false);
				result = elasticsearchService.specialSearch(queryParam, pager.getStartRow(), pager.getPageSize());
				return buildReturnResult(result, pager);
			}else if("searchAll".equals(apiName)) {
				setQueryParams(params, pager, false, false);
				List<Map<String, Object>> allList = elasticsearchService.searchAll(queryParam, pager.getStartRow(), pager.getPageSize());
				return new PageResult(JSON.toJSONString(allList), allList.size(), pager.getPageSize());
			}else if("fuzzySearch".equals(apiName)) {
				setQueryParams(params, pager, true, true);
				result = elasticsearchService.fuzzySearch(queryParam, pager.getStartRow(), pager.getPageSize());
				return buildReturnResult(result, pager);
			}else {
				throw new ApiException("访问的接口不存在");
			}
		} catch (Exception e) {
			logger.error("全文检索失败!", e);
			throw new ApiException("全文检索失败 ", e);
		}
    	
    }

	private PageResult buildReturnResult(ListResult<Map<String, ?>> result, Pagination pager) {
		if(null == result) {
			result = new ListResult<>();
		}
		return new PageResult(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect), result.getCount().intValue(), pager.getPageSize());
	}

	private void setQueryParams(Map<String, String> params, Pagination pager, boolean hasKeywords, boolean isFuzzy) throws ApiException {
		try {
			if(!params.containsKey("index")){
				throw new ApiException("index参数为必输项");
			}
			if(!params.containsKey("type")){
				throw new ApiException("type参数为必输项");
			}
			String index = params.get("index");
			String type = params.get("type");
			String size = params.get("pageSize");
			Integer pageSize = null;
			//是否有关键词，不带查询所有记录
			if(hasKeywords) {
				if(!params.containsKey("keywords")){
					throw new ApiException("keywords参数为必输项");
				}
				String keywords = params.get("keywords");
				logger.debug("===========keywords============== " + keywords);
				if(isFuzzy) {
					queryParam.setKeywords("*" + keywords + "*");
				}else {
					queryParam.setKeywords(keywords);
				}
				pageSize =  StringUtil.isBlank(size) ? 20 : Integer.parseInt(size);
			}else {
				pageSize = StringUtil.isBlank(size) ? 5000 : Integer.parseInt(size);
			}
			pager.setPageSize(pageSize);
			
			queryParam.setIndices(index);
			queryParam.setTypes(type);
		} catch (Exception e) {
			logger.error("格式转换异常", e);
			e.printStackTrace();
			throw new ApiException("");
		}
	}

	@Override
	public PageResult outXml(int arg0, String arg1, Map<String, String> arg2) throws ApiException {
		return null;
	}
	
}
