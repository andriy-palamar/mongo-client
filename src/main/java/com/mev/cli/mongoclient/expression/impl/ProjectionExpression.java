package com.mev.cli.mongoclient.expression.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component("projectionExpression")
public class ProjectionExpression extends Expression<AggregationOperation> {

	@Override
	public AggregationOperation interpret(Statement statement) {
		if (statement instanceof Select) {
			return getAggregationOperation(statement);
		} else {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		}
	}
	
	private AggregationOperation getAggregationOperation(Statement statement) {
		PlainSelect plainSelect = ExpressionUtil.getPlainSelect(statement);

		List<String> projections = getProjections(plainSelect);
		
		if (projections == ExpressionUtil.EMPTY_PROJECTIONS) {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		}

		String[] projectionsArray = new String[projections.size()];
		projectionsArray = projections.toArray(projectionsArray);
		
		return Aggregation.project(projectionsArray);
	}
	
	private List<String> getProjections(PlainSelect plainSelect) {
		List<String> projections = plainSelect.getSelectItems().stream()
				.map((item) -> item.toString())
				.collect(Collectors.toList());
		
		return ExpressionUtil.processProjections(projections);
	}
}
