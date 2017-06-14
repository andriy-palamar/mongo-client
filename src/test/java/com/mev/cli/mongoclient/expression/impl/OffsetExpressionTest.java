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
public class OffsetExpressionTest {

	private static final String OFFSET_CONDITION_QUERY = "SELECT * FROM user limit 2 offset 5;";
	private static final String EXPECTED_OFFSET_CONDITION_QUERY = "{ \"$skip\" : 5}";

	private static final String OFFSET_WRONG_CONDITION_QUERY = "SELECT * FROM user offset 5;";

	private static final String OFFSET_NOT_PRESENT_CONDITION_QUERY = "SELECT * FROM user";

	@Autowired
	@Qualifier("offsetExpression")
	private Expression<AggregationOperation> offsetExpression;

	@Test
	public void whenOffsetIsPresentTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(OFFSET_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = offsetExpression.interpret(statement);

		// assert
		DBObject dbObject = aggregationOperation.toDBObject(Aggregation.DEFAULT_CONTEXT);

		Assert.assertEquals(EXPECTED_OFFSET_CONDITION_QUERY, dbObject.toString());
	}

	@Test(expected = TextProcessorException.class)
	public void whenOffsetWithoutLimitTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(OFFSET_WRONG_CONDITION_QUERY);

		// act
		offsetExpression.interpret(statement);
	}

	@Test
	public void whenOffsetIsNotPresentTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(OFFSET_NOT_PRESENT_CONDITION_QUERY);

		// act
		AggregationOperation aggregationOperation = offsetExpression.interpret(statement);

		// assert
		Assert.assertNull(aggregationOperation);
	}
}
