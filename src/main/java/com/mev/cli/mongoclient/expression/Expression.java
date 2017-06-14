package com.mev.cli.mongoclient.expression;

import com.mev.cli.mongoclient.exception.TextProcessorException;

import net.sf.jsqlparser.statement.Statement;

public abstract class Expression<T> {
	
	/**
	 * Interprets query. Retrieves needed part of query as object.
	 * @param statement - specified query.
	 * @return specified object.
	 * @throws TextProcessorException 
	 */
	public abstract T interpret(Statement statement) throws TextProcessorException;
	
}
