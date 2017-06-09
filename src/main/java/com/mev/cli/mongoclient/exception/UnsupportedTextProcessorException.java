package com.mev.cli.mongoclient.exception;

/**
 * Exception for query validation.
 */
public class UnsupportedTextProcessorException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnsupportedTextProcessorException() {
		super();
	}

	public UnsupportedTextProcessorException(String message) {
		super(message);
	}

	public UnsupportedTextProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedTextProcessorException(Throwable cause) {
		super(cause);
	}
}
