package com.mev.cli.mongoclient.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectSQLValidatorTest {
	
	@Autowired
	private SQLValidator sqlValidator;

	private static final String CORRECT_QUERY_WITH_CONDITION_AND_ORDER_BY_AND_LIMIT 
		= "SELECT * FROM user WHERE name = 'as' AND age > 5 order by age, name desc limit 2 offset 10;";
	
	private static final String QUERY_WITH_INCORRECT_LIMIT 
		= "SELECT * FROM user WHERE name = 'as' AND age > 5 offset 4;";
	
	private static final String QUERY_WITH_INCORRECT_CONDITION 
		= "SELECT * FROM user WHERE name = 'as' AND;";
	
	private static final String QUERY_WITH_INCORRECT_ORDER_BY
		= "SELECT * FROM user WHERE name = 'as' order by name descendent;";

	@Test
	public void whenCorrectQueryWithConditionAndOrderByAndLimitTest() {
		//assert
		Assert.assertTrue(sqlValidator.isValid(CORRECT_QUERY_WITH_CONDITION_AND_ORDER_BY_AND_LIMIT));
	}
	
	@Test
	public void whenQueryWithIncorrectLimitTest() {
		//assert
		Assert.assertTrue(!sqlValidator.isValid(QUERY_WITH_INCORRECT_LIMIT));
	}
	
	@Test
	public void whenQueryWithIncorrectConditionTest() {
		//assert
		Assert.assertTrue(!sqlValidator.isValid(QUERY_WITH_INCORRECT_CONDITION));
	}
	
	@Test
	public void whenQueryWithIncorrectOrderByTest() {
		//assert
		Assert.assertTrue(!sqlValidator.isValid(QUERY_WITH_INCORRECT_ORDER_BY));
	}
}
