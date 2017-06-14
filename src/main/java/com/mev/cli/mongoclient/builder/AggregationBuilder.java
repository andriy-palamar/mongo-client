package com.mev.cli.mongoclient.builder;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.util.ExpressionUtil;

@Component
public class AggregationBuilder {
	private List<AggregationOperation> aggregationOperations = new ArrayList<>();

	public AggregationBuilder addAggregationOperation(AggregationOperation aggregationOperation) {
		Optional.ofNullable(aggregationOperation).ifPresent(agg -> aggregationOperations.add(agg));
		
		return this;
	}

	public Aggregation build() {
		Aggregation aggregation = aggregationOperations.size() == 0 ? 
				ExpressionUtil.EMPTY_AGGREGATION : newAggregation(aggregationOperations);
		
		aggregationOperations = new ArrayList<>();

		return aggregation;
	}

}