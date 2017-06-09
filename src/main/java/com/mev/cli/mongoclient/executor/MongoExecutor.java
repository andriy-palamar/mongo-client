package com.mev.cli.mongoclient.executor;

import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;

public interface MongoExecutor<T> {
	
	T execute(String query) throws TextProcessorException, TextProcessorFactoryException, UnsupportedTextProcessorException;
	
}
