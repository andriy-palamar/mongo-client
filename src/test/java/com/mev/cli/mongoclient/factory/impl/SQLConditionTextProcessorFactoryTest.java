package com.mev.cli.mongoclient.factory.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SQLConditionTextProcessorFactoryTest {
	private static final String UNSUPPORTED_QUERY = "age & '5'";

	private static final String EQUALS_QUERY = "age = '5'";
	private static final String EXPECTED_EQUALS_QUERY = "{ \"age\" : 5.0}";

	private static final String GREATER_QUERY = "age > 5";
	private static final String EXPECTED_GREATER_QUERY = "{ \"age\" : { \"$gt\" : 5.0}}";

	private static final String GREATER_THAN_EQUALS_QUERY = "age >= 5";
	private static final String EXPECTED_GREATER_THAN_EQUALS_QUERY = "{ \"age\" : { \"$gte\" : 5.0}}";
	
	private static final String LESS_QUERY = "age < 5";
	private static final String EXPECTED_LESS_QUERY = "{ \"age\" : { \"$lt\" : 5.0}}";

	private static final String LESS_THAN_EQUALS_QUERY = "age <= 5";
	private static final String EXPECTED_LESS_THAN_EQUALS_QUERY = "{ \"age\" : { \"$lte\" : 5.0}}";

	private static final String NOT_EQUALS1_QUERY = "age <> 5";
	private static final String NOT_EQUALS2_QUERY = "age != 5";
	private static final String EXPECTED_NOT_EQUALS_QUERY = "{ \"age\" : { \"$ne\" : 5.0}}";

	@Autowired
	@Qualifier("sqlConditionTextProcessorFactory")
	private TextProcessorFactory<Criteria> sqlConditionTextProcessorFactory;

	@Test(expected = TextProcessorFactoryException.class)
	public void whenUnsupportedQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		sqlConditionTextProcessorFactory.getObjectByQuery(UNSUPPORTED_QUERY);
	}
	
	@Test(expected = TextProcessorFactoryException.class)
	public void whenGetObjectByNullQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		sqlConditionTextProcessorFactory.getObjectByQuery(null);
	}

	@Test(expected = TextProcessorFactoryException.class)
	public void whenGetObjectByEmptyQueryTest()
			throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		sqlConditionTextProcessorFactory.getObjectByQuery("");
	}

	@Test
	public void whenEqualsQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria = sqlConditionTextProcessorFactory.getObjectByQuery(EQUALS_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_EQUALS_QUERY, resultCriteria.getCriteriaObject().toString());
	}

	@Test
	public void whenGreaterQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria = sqlConditionTextProcessorFactory.getObjectByQuery(GREATER_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_GREATER_QUERY, resultCriteria.getCriteriaObject().toString());
	}

	@Test
	public void wheGreaterThanEqualsQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria = sqlConditionTextProcessorFactory.getObjectByQuery(GREATER_THAN_EQUALS_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_GREATER_THAN_EQUALS_QUERY, resultCriteria.getCriteriaObject().toString());
	}

	@Test
	public void whenLessQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria = sqlConditionTextProcessorFactory.getObjectByQuery(LESS_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_LESS_QUERY, resultCriteria.getCriteriaObject().toString());
	}

	@Test
	public void whenLessThanEqualsQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria = sqlConditionTextProcessorFactory.getObjectByQuery(LESS_THAN_EQUALS_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_LESS_THAN_EQUALS_QUERY, resultCriteria.getCriteriaObject().toString());
	}

	@Test
	public void whenNotEqualsQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		Criteria resultCriteria1 = sqlConditionTextProcessorFactory.getObjectByQuery(NOT_EQUALS1_QUERY);
		Criteria resultCriteria2 = sqlConditionTextProcessorFactory.getObjectByQuery(NOT_EQUALS2_QUERY);
		
		// assert
		Assert.assertEquals(EXPECTED_NOT_EQUALS_QUERY, resultCriteria1.getCriteriaObject().toString());
		Assert.assertEquals(EXPECTED_NOT_EQUALS_QUERY, resultCriteria2.getCriteriaObject().toString());
	}
}
