package com.mev.cli.mongoclient.processor.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.builder.AggregationBuilder;
import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.processor.SQLTextProcessor;
import com.mev.cli.mongoclient.validator.SQLValidator;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@Component
public class SelectSQLTextProcessor implements SQLTextProcessor<Aggregation> {

	private AggregationBuilder aggregationBuilder;

	private SQLValidator sqlValidator;

	private Expression<AggregationOperation> projectionExpression;

	private Expression<AggregationOperation> conditionExpression;

	private Expression<AggregationOperation> orderByExpression;

	private Expression<AggregationOperation> offsetExpression;

	private Expression<AggregationOperation> limitExpression;

	@Autowired
	public SelectSQLTextProcessor(AggregationBuilder aggregationBuilder, SQLValidator sqlValidator) {
		this.aggregationBuilder = aggregationBuilder;
		this.sqlValidator = sqlValidator;
	}

	@Override
	public Aggregation process(String query) throws TextProcessorException {
		if (!sqlValidator.isValid(query)) {
			throw new TextProcessorException();
		}

		try {
			Statement statement = CCJSqlParserUtil.parse(query);

			return aggregationBuilder
					.addAggregationOperation(offsetExpression.interpret(statement))
					.addAggregationOperation(limitExpression.interpret(statement))
					.addAggregationOperation(conditionExpression.interpret(statement))
					.addAggregationOperation(projectionExpression.interpret(statement))
					.addAggregationOperation(orderByExpression.interpret(statement))
					.build();
		} catch (JSQLParserException e) {
			throw new TextProcessorException();
		}
	}

	@Autowired
	@Qualifier("projectionExpression")
	public void setProjectionExpression(Expression<AggregationOperation> projectionExpression) {
		this.projectionExpression = projectionExpression;
	}

	@Autowired
	@Qualifier("conditionExpression")
	public void setConditionExpression(Expression<AggregationOperation> conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	@Autowired
	@Qualifier("orderByExpression")
	public void setOrderByExpression(Expression<AggregationOperation> orderByExpression) {
		this.orderByExpression = orderByExpression;
	}

	@Autowired
	@Qualifier("offsetExpression")
	public void setOffsetExpression(Expression<AggregationOperation> offsetExpression) {
		this.offsetExpression = offsetExpression;
	}

	@Autowired
	@Qualifier("limitExpression")
	public void setLimitExpression(Expression<AggregationOperation> limitExpression) {
		this.limitExpression = limitExpression;
	}

}
