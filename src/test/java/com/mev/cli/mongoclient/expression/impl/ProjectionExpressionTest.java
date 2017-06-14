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
public class ProjectionExpressionTest {

	private static final String PROJECTION_CONDITION_QUERY = "SELECT name, surname, hobby.* FROM user;";
	private static final String EXPECTED_PROJECTION_CONDITION_QUERY = "{ \"$project\" : { \"name\" : 1 , \"surname\" : 1 , \"hobby\" : 1}}";

	private static final String PROJECTION_NOT_PRESENT_CONDITION_QUERY = "SELECT * FROM user";

	@Autowired
	@Qualifier("projectionExpression")
	private Expression<AggregationOperation> projectionExpression;
	
	@Test
	public void whenQueryHasDifferentProjectionsTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(PROJECTION_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = projectionExpression.interpret(statement);

		// assert
		DBObject dbObject = aggregationOperation.toDBObject(Aggregation.DEFAULT_CONTEXT);
		
		Assert.assertEquals(EXPECTED_PROJECTION_CONDITION_QUERY, dbObject.toString());
	}
	
	@Test
	public void whenProjectionIsAsteriskTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(PROJECTION_NOT_PRESENT_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = projectionExpression.interpret(statement);

		// assert
		Assert.assertNull(aggregationOperation);
	}
}
