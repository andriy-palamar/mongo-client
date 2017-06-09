package com.mev.cli.mongoclient.exception;

/**
 * Exception for query validation.
 */
public class TextProcessorException extends Exception {
	private static final long serialVersionUID = 1L;

	public TextProcessorException() {
		super();
	}

	public TextProcessorException(String message) {
		super(message);
	}

	public TextProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextProcessorException(Throwable cause) {
		super(cause);
	}
}
