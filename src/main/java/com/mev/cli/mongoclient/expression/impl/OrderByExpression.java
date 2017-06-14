package com.mev.cli.mongoclient.expression.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.enums.SQLKeywordEnum;
import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component("orderByExpression")
public class OrderByExpression extends Expression<AggregationOperation> {

	@Override
	public AggregationOperation interpret(Statement statement) {
		if (statement instanceof Select) {
			return getOrderByOperationFromSelect(statement);
		} else {
			return ExpressionUtil.EMPTY_AGGREGATION_OPERATION;
		}
	}

	private AggregationOperation getOrderByOperationFromSelect(Statement statement) {
		PlainSelect plainSelect = ExpressionUtil.getPlainSelect(statement);
		
		return Optional.ofNullable(getOrderByOperations(plainSelect))
			.map(orderByOperations -> {
				String[] orderByOperationsArray = new String[orderByOperations.size()];
				orderByOperationsArray = orderByOperations.toArray(orderByOperationsArray);

				String lastOrderByItem = orderByOperationsArray[orderByOperationsArray.length - 1];
				Sort.Direction sortDirection;
				if (lastOrderByItem.contains(SQLKeywordEnum.DESC.toString())) {
					sortDirection = Sort.Direction.DESC;
				} else {
					sortDirection = Sort.Direction.ASC;
				}

				orderByOperationsArray[orderByOperationsArray.length - 1] = lastOrderByItem
						.replaceAll(SQLKeywordEnum.DESC.toString() + "|" + SQLKeywordEnum.ASC.toString(), "").trim();

				return Aggregation.sort(sortDirection, orderByOperationsArray);
			}).orElse(null);
	}

	private List<String> getOrderByOperations(PlainSelect plainSelect) {
		return Optional.ofNullable(plainSelect.getOrderByElements())
				.map(orderByElements -> {
					return orderByElements.stream()
							.map((item) -> item.toString())
							.collect(Collectors.toList());
				}).orElse(null);
	}

}
