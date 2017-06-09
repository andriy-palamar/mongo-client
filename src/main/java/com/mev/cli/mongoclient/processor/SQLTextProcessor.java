package com.mev.cli.mongoclient.processor;

import com.mev.cli.mongoclient.exception.TextProcessorException;

public interface SQLTextProcessor<T> {
	
	/**
	 * Is intended to process text and convert it to object.
	 * @param text - specified text.
	 * @return resulted object.
	 */
	T process(String text) throws TextProcessorException;
	
}
