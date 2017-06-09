package com.mev.cli.mongoclient.factory.impl;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;
import com.mev.cli.mongoclient.processor.SQLTextProcessor;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

@Component("sqlTextProcessorFactory")
public class SQLTextProcessorFactory implements TextProcessorFactory<SQLTextProcessor<Aggregation>> {

	private SQLTextProcessor<Aggregation> selectSQLTextProcessor;

	@Autowired
	public SQLTextProcessorFactory(SQLTextProcessor<Aggregation> selectSQLTextProcessor) {
		this.selectSQLTextProcessor = selectSQLTextProcessor;
	}

	@Override
	public SQLTextProcessor<Aggregation> getObjectByQuery(String query)
			throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		JSqlParser sqlParser = new CCJSqlParserManager();
		try {
			Statement statement = sqlParser.parse(new StringReader(query));
			if (statement instanceof Select) {
				return selectSQLTextProcessor;
			} else {
				throw new UnsupportedTextProcessorException("Cannot identify type of query.");
			}
		} catch (JSQLParserException e) {
			throw new TextProcessorFactoryException("Can not parse query.", e);
		}
	}
}
