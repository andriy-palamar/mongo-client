package com.mev.cli.mongoclient.expression.impl;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.exception.TextProcessorException;
import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component("offsetExpression")
public class OffsetExpression extends Expression<AggregationOperation> {

	@Override
	public AggregationOperation interpret(Statement statement) throws TextProcessorException {
		if (statement instanceof Select) {
			return getAggregationOperation(statement);
		} else {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		}
	}

	private AggregationOperation getAggregationOperation(Statement statement) throws TextProcessorException {
		PlainSelect plainSelect = ExpressionUtil.getPlainSelect(statement);
		
		Limit limit = plainSelect.getLimit();

		if (limit == ExpressionUtil.EMPTY_AGGREGATION_OPERATION) {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		} else if (limit.getRowCount() == 0L) {
			throw new TextProcessorException();
		} else {
			return Aggregation.skip(limit.getOffset());
		}
	}

}
