package com.mev.cli.mongoclient.executor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;
import com.mev.cli.mongoclient.executor.MongoExecutor;
import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;
import com.mev.cli.mongoclient.processor.SQLTextProcessor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@Component
public class SQLQueryMongoExecutor implements MongoExecutor<List<String>> {

	private MongoTemplate mongoTemplate;

	private TextProcessorFactory<SQLTextProcessor<Aggregation>> processorFactory;

	private Expression<String> targetExpression;

	private MongoDatabase mongoDatabase;

	@Autowired
	public SQLQueryMongoExecutor(MongoTemplate mongoTemplate,
			@Qualifier("sqlTextProcessorFactory") TextProcessorFactory<SQLTextProcessor<Aggregation>> processorFactory,
			@Qualifier("targetExpression") Expression<String> targetExpression, MongoDatabase mongoDatabase) {
		this.mongoTemplate = mongoTemplate;
		this.processorFactory = processorFactory;
		this.targetExpression = targetExpression;
		this.mongoDatabase = mongoDatabase;
	}

	@Override
	public List<String> execute(String query)
			throws TextProcessorException, TextProcessorFactoryException, UnsupportedTextProcessorException {
		SQLTextProcessor<Aggregation> sqlTextProcessor = processorFactory.getObjectByQuery(query);

		try {
			Statement statement = CCJSqlParserUtil.parse(query);

			String collectionName = targetExpression.interpret(statement);

			Aggregation aggregation = sqlTextProcessor.process(query);
			
			return Optional.ofNullable(aggregation)
					.map(agg -> getDocumentsFromCollectionByAggregation(agg, collectionName))
	                .orElse(getAllDocumentsFromCollection(collectionName));
		} catch (JSQLParserException e) {
			throw new TextProcessorException(e);
		}
	}

	private List<String> getDocumentsFromCollectionByAggregation(Aggregation aggregation, String collectionName) {
		List<String> jsonResults = new ArrayList<>();

		AggregationResults<Object> aggregationResults = mongoTemplate.aggregate(aggregation, collectionName,
				Object.class);
		aggregationResults.forEach(a -> jsonResults.add(a.toString()));

		return jsonResults;
	}

	private List<String> getAllDocumentsFromCollection(String tableName) {
		List<String> jsonResults = new ArrayList<>();

		FindIterable<Document> findIterable = mongoDatabase.getCollection(tableName).find();

		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			jsonResults.add(mongoCursor.next().toJson());
		}

		return jsonResults;
	}
}
