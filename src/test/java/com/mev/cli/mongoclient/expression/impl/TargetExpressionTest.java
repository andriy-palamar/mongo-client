package com.mev.cli.mongoclient.expression.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.expression.Expression;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetExpressionTest {

	private static final String TABLE_NAME_CONDITION_QUERY = "SELECT name, surname, hobby.* FROM user;";
	private static final String EXPECTED_TABLE_NAME_CONDITION_QUERY = "user";

	@Autowired
	@Qualifier("targetExpression")
	private Expression<String> targetExpression;
	
	@Test
	public void whenTableNameIsUserTest() throws JSQLParserException, TextProcessorException {
		// arrange
		Statement statement = CCJSqlParserUtil.parse(TABLE_NAME_CONDITION_QUERY);

		// act
		String tableName = targetExpression.interpret(statement);
		
		//assert
		Assert.assertEquals(EXPECTED_TABLE_NAME_CONDITION_QUERY, tableName);
	}
}
