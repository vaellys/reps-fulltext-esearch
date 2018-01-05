package com.reps.es.exception;

import com.reps.core.exception.RepsException;

public class ElasticsearchException extends RepsException{
	
	private static final long serialVersionUID = 1L;

	public ElasticsearchException() {
		super();
	}

	public ElasticsearchException(String msg, int code, Throwable e) {
		super(msg, code, e);
	}

	public ElasticsearchException(String msg, int code) {
		super(msg, code);
	}

	public ElasticsearchException(String msg, Throwable exp) {
		super(msg, exp);
	}

	public ElasticsearchException(String msg) {
		super(msg);
	}

	public ElasticsearchException(Throwable e) {
		super(e);
	}
	
}
