package com.mev.cli.mongoclient.expression;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.test.context.junit4.SpringRunner;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConditionExpressionTest {

	private static final String CONDITION_QUERY = "SELECT * FROM user WHERE (name = 'as' AND age > 5) or surname = 'kul';";

	@Autowired
	@Qualifier("conditionExpression")
	private Expression<AggregationOperation> conditionExpression;
	
	@Test
	public void interpretTest() throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(CONDITION_QUERY);

		AggregationOperation aggregationOperation = conditionExpression.interpret(statement);

		assertThat(aggregationOperation.toString()).isEqualTo("(name = 'as' AND age > 5) or surname = 'kul'");
	}
}
