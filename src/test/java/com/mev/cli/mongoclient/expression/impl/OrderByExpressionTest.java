package com.mev.cli.mongoclient.expression.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.expression.Expression;
import com.mongodb.DBObject;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderByExpressionTest {

	private static final String ORDER_BY_CONDITION_QUERY = "SELECT * FROM user order by age desc;";
	private static final String EXPECTED_ORDER_BY_CONDITION_QUERY = "{ \"$sort\" : { \"age\" : -1}}";

	private static final String ORDER_BY_DIFFERENT_PARAMETERS_CONDITION_QUERY = "SELECT * FROM user order by age, name, hobby desc;";
	private static final String EXPECTED_ORDER_BY_DIFFERENT_PARAMETERS_CONDITION_QUERY = "{ \"$sort\" : { \"age\" : -1 , \"name\" : -1 , \"hobby\" : -1}}";

	private static final String ORDER_BY_NOT_PRESENT_CONDITION_QUERY = "SELECT * FROM user";

	@Autowired
	@Qualifier("orderByExpression")
	private Expression<AggregationOperation> orderByExpression;

	@Test
	public void whenOrderByIsPresentTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(ORDER_BY_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = orderByExpression.interpret(statement);

		// assert
		DBObject dbObject = aggregationOperation.toDBObject(Aggregation.DEFAULT_CONTEXT);

		Assert.assertEquals(EXPECTED_ORDER_BY_CONDITION_QUERY, dbObject.toString());
	}

	@Test
	public void whenOrderByDifferentParametersTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(ORDER_BY_DIFFERENT_PARAMETERS_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = orderByExpression.interpret(statement);

		// assert
		DBObject dbObject = aggregationOperation.toDBObject(Aggregation.DEFAULT_CONTEXT);
		
		Assert.assertEquals(EXPECTED_ORDER_BY_DIFFERENT_PARAMETERS_CONDITION_QUERY, dbObject.toString());
	}

	@Test
	public void whenOrderByIsNotPresentTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(ORDER_BY_NOT_PRESENT_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = orderByExpression.interpret(statement);

		// assert
		Assert.assertNull(aggregationOperation);
	}
}
