package com.mev.cli.mongoclient.expression.impl;

import java.util.Optional;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component("limitExpression")
public class LimitExpression extends Expression<AggregationOperation> {

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
		
		Limit limit = plainSelect.getLimit();
		
		return Optional.ofNullable(limit)
				.map(lim -> Aggregation.limit(limit.getRowCount()))
				.orElse(null);
	}

}
