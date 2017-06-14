package com.mev.cli.mongoclient.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

@RunWith(MockitoJUnitRunner.class)
public class AggregationBuilderTest {

	@InjectMocks
	private AggregationBuilder aggregationBuilder;

	@Spy
	private List<AggregationOperation> aggregationOperations = new ArrayList<>();

	@Mock
	private AggregationOperation aggregationOperationMock;

	@Test
	public void testAddNullAggregationOperation() {
		// act
		AggregationBuilder actualAggregationBuilder = aggregationBuilder.addAggregationOperation(null);

		// assert
		Assert.assertEquals(actualAggregationBuilder, aggregationBuilder);
		Mockito.verify(aggregationOperations, Mockito.never()).add(Mockito.any());
	}

	@Test
	public void testAddNotNullAggregationOperation() {
		// act
		AggregationBuilder actualAggregationBuilder = aggregationBuilder
				.addAggregationOperation(aggregationOperationMock);

		// assert
		Assert.assertEquals(actualAggregationBuilder, aggregationBuilder);
		Mockito.verify(aggregationOperations).add(Mockito.any());

		Mockito.verifyNoMoreInteractions(aggregationOperations);
	}

	@Test
	public void testBuildAggregation() {
		// act
		AggregationBuilder actualAggregationBuilder = aggregationBuilder.addAggregationOperation(aggregationOperationMock);
		Aggregation aggregation = actualAggregationBuilder.build();

		// assert
		Assert.assertNotNull(aggregation);
	}
}
