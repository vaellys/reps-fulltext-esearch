package com.reps.es.service;

import com.reps.es.exception.ElasticsearchException;

public interface IRebuildIndexService {

	public void rebuildIndex() throws ElasticsearchException;

}
