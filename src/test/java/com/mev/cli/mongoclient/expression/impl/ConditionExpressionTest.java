package com.mev.cli.mongoclient.expression.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.expression.Expression;
import com.mongodb.DBObject;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConditionExpressionTest {

	private static final String AND_SELECT_CONDITION_QUERY = "SELECT * FROM user WHERE name = 'as' AND age > 5;";
	private static final String EXPECTED_AND_SELECT_CONDITION_QUERY = "{ \"$match\" : { \"$and\" : [ { \"name\" : \"as\"} , { \"age\" : { \"$gt\" : 5.0}}]}}";

	private static final String OR_SELECT_CONDITION_QUERY = "SELECT * FROM user WHERE age < 5 or age > 10;";
	private static final String EXPECTED_OR_SELECT_CONDITION_QUERY = "{ \"$match\" : { \"$or\" : [ { \"age\" : { \"$lt\" : 5.0}} , { \"age\" : { \"$gt\" : 10.0}}]}}";

	private static final String SELECT_NOT_EQUAL_CONDITION_QUERY = "SELECT * FROM user WHERE name <> 'NAME';";
	private static final String EXPECTED_SELECT_NOT_EQUAL_CONDITION_QUERY = "{ \"$match\" : { \"name\" : { \"$ne\" : \"NAME\"}}}";
	
	private static final String CONDITION_NOT_PRESENT_QUERY = "SELECT * FROM user;";

	@Autowired
	@Qualifier("conditionExpression")
	private Expression<AggregationOperation> conditionExpression;

	@MockBean
	private AbstractEvaluator<Criteria> sqlConditionEvaluator;

	@Test
	public void whenConditionNameIsAsAndAgeGt5Test() throws JSQLParserException, TextProcessorException {
		Criteria criteria = new Criteria();
		Criteria resultCiteria = criteria.andOperator(Criteria.where("name").is("as"), Criteria.where("age").gt(5.0));
		
		testCondition(resultCiteria, "name = 'as' AND age > 5", AND_SELECT_CONDITION_QUERY,
				EXPECTED_AND_SELECT_CONDITION_QUERY);
	}

	@Test
	public void whenConditionAgeLt5OrAgeGt10Test() throws JSQLParserException, TextProcessorException {
		Criteria criteria = new Criteria();
		Criteria resultCiteria = criteria.orOperator(Criteria.where("age").lt(5.0), Criteria.where("age").gt(10.0));
		
		testCondition(resultCiteria, "age < 5 OR age > 10", OR_SELECT_CONDITION_QUERY,
				EXPECTED_OR_SELECT_CONDITION_QUERY);
	}

	@Test
	public void whenConditionNameNotEqualNameTest() throws JSQLParserException, TextProcessorException {
		Criteria resultCiteria = Criteria.where("name").ne("NAME");
		
		testCondition(resultCiteria, "name <> 'NAME'", SELECT_NOT_EQUAL_CONDITION_QUERY,
				EXPECTED_SELECT_NOT_EQUAL_CONDITION_QUERY);
	}

	private void testCondition(Criteria criteria, String expression, String query, String expectedQuery)
			throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(query);

		Mockito.when(sqlConditionEvaluator.evaluate(expression)).thenReturn(criteria);

		// act
		AggregationOperation aggregationOperation = conditionExpression.interpret(statement);

		// assert
		DBObject dbObject = aggregationOperation.toDBObject(Aggregation.DEFAULT_CONTEXT);

		Mockito.verify(sqlConditionEvaluator).evaluate(expression);
		Assert.assertEquals(expectedQuery, dbObject.toString());
	}
	
	@Test
	public void whenConditionIsNotPresentTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(CONDITION_NOT_PRESENT_QUERY);

		// act
		AggregationOperation aggregationOperation = conditionExpression.interpret(statement);

		// assert
		Assert.assertNull(aggregationOperation);
	}
}
