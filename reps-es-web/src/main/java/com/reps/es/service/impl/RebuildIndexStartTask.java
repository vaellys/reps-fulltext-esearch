package com.reps.es.service.impl;

import static com.reps.es.util.LoadResourcesUtil.getResources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reps.core.RepsConstant;
import com.reps.core.util.StringUtil;
import com.reps.es.exception.ElasticsearchException;
import com.reps.es.service.IElasticsearchService;
import com.reps.es.service.IRebuildIndexService;
import com.reps.es.util.IndicesManager;

@Service("rebuildIndexStartTask")
public class RebuildIndexStartTask implements IRebuildIndexService{
	
	@Autowired
	private IndicesManager indicesManager;
	
	@Autowired
	private IElasticsearchService elasticsearchService;
	
	@Autowired
	private IRebuildIndexService rebuildIndexService;
	
	@Override
	public void rebuildIndex() throws ElasticsearchException {
		String index = RepsConstant.getContextProperty("es.rebuild.index.name");
		String indexType = RepsConstant.getContextProperty("es.index.type");
		String[] types = indexType.split(",");
		//删除索引时索引不存在会在创建索引映射时创建
		elasticsearchService.deleteIndex(index);
		if(StringUtil.isNotBlank(indexType)) {
			for (String type : types) {
				elasticsearchService.addIndexMapping(index, type, indicesManager.getIndexSetting(), getResources());
			}
		}else {
			elasticsearchService.addIndexMapping(index, null, indicesManager.getIndexSetting(), getResources());
		}
		//新建索引映射结构
		rebuildIndexService.rebuildIndex();
	}
}
