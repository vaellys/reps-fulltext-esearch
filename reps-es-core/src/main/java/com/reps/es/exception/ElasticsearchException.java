package com.reps.es.exception;

public class ElasticsearchException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ElasticsearchException() {
		super();
	}

	public ElasticsearchException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ElasticsearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElasticsearchException(String message) {
		super(message);
	}

	public ElasticsearchException(Throwable cause) {
		super(cause);
	}
	
}
