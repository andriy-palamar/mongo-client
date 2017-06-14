package com.mev.cli.mongoclient.expression.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component("conditionExpression")
public class ConditionExpression extends com.mev.cli.mongoclient.expression.Expression<AggregationOperation> {

	private AbstractEvaluator<Criteria> sqlConditionEvaluator;

	@Autowired
	public ConditionExpression(AbstractEvaluator<Criteria> sqlConditionEvaluator) {
		this.sqlConditionEvaluator = sqlConditionEvaluator;
	}

	@Override
	public AggregationOperation interpret(Statement statement) {
		if (statement instanceof Select) {
			return getConditionOperationFromSelect(statement);
		} else {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		}
	}

	private AggregationOperation getConditionOperationFromSelect(Statement statement) {
		PlainSelect plainSelect = ExpressionUtil.getPlainSelect(statement);
		
		return Optional.ofNullable(plainSelect.getWhere()).map(expression -> {
			String condition = expression.toString();

			Criteria criteria = sqlConditionEvaluator.evaluate(condition);

			return Optional.ofNullable(criteria)
					.map(cr -> Aggregation.match(cr))
					.orElse(null);
		}).orElse(null);
	}

}
