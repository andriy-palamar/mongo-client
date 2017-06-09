package com.mev.cli.mongoclient.exception;

/**
 * Exception for query validation.
 */
public class TextProcessorFactoryException extends Exception {
	private static final long serialVersionUID = 1L;

	public TextProcessorFactoryException() {
		super();
	}

	public TextProcessorFactoryException(String message) {
		super(message);
	}

	public TextProcessorFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextProcessorFactoryException(Throwable cause) {
		super(cause);
	}
}
