package com.mev.cli.mongoclient.factory;

import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;

public interface TextProcessorFactory<T> {

	T getObjectByQuery(String query) throws TextProcessorFactoryException, UnsupportedTextProcessorException;

}
