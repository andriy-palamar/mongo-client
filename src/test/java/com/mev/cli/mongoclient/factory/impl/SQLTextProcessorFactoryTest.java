package com.mev.cli.mongoclient.factory.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;
import com.mev.cli.mongoclient.processor.SQLTextProcessor;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SQLTextProcessorFactoryTest {
	private static final String CORRECT_SELECT_QUERY = "SELECT * FROM user WHERE name = 'as' AND age > 5;";
	private static final String INCORRECT_SELECT_QUERY = "SELECT * FROM WHERE name = 'as' AND age > 5;";
	private static final String CORRECT_UPDATE_QUERY = "UPDATE user SET name = 'Andrii' WHERE age = 5;";

	@Autowired
	@Qualifier("sqlTextProcessorFactory")
	private TextProcessorFactory<SQLTextProcessor<Aggregation>> sqlTextProcessorFactory;

	@Test(expected = UnsupportedTextProcessorException.class)
	public void whenUpdateQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		sqlTextProcessorFactory.getObjectByQuery(CORRECT_UPDATE_QUERY);
	}

	@Test
	public void whenCorrectSelectQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		SQLTextProcessor<Aggregation> sqlTextProcessor = sqlTextProcessorFactory.getObjectByQuery(CORRECT_SELECT_QUERY);
		
		//assert
		Assert.assertNotNull(sqlTextProcessor);
	}

	@Test(expected = TextProcessorFactoryException.class)
	public void whenIncorrectSelectQueryTest() throws TextProcessorFactoryException, UnsupportedTextProcessorException {
		// act
		SQLTextProcessor<Aggregation> sqlTextProcessor = sqlTextProcessorFactory.getObjectByQuery(INCORRECT_SELECT_QUERY);
		
		//assert
		Assert.assertNotNull(sqlTextProcessor);
	}
}
