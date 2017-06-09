package com.mev.cli.mongoclient.expression;

import net.sf.jsqlparser.statement.Statement;

public abstract class Expression<T> {
	
	/**
	 * Interprets query. Retrieves needed part of query as object.
	 * @param statement - specified query.
	 * @return specified object.
	 */
	public abstract T interpret(Statement statement);
	
}
