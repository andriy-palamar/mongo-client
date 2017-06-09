package com.mev.cli.mongoclient.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class ExpressionUtil {

	public static final Aggregation EMPTY_AGGREGATION = null;

	public static final Object EMPTY_EXPRESSION = null;
	
	public static final AggregationOperation EMPTY_AGGREGATION_OPERATION = null;
	
	public static final List<String> EMPTY_PROJECTIONS = null;

	public static final String NO_TABLE_NAME = null;

	private static final String ASTERISK = "*";
	
	public static PlainSelect getPlainSelect(Statement statement) {
		Select select = (Select) statement;
		return  (PlainSelect) select.getSelectBody();
	}
	
	public static List<String> processProjections(List<String> projections) {
		if (projections.size() == 0) {
			return EMPTY_PROJECTIONS;
		} else if (projections.size() == 1) {
			String projection = projections.get(0);
			
			if (projection.contains(ASTERISK)) {
				return EMPTY_PROJECTIONS;
			} else {
				return projections;
			}
		} else {
			return projections.stream()
					.map((p) -> p.replaceFirst("\\.\\*", ""))
					.collect(Collectors.toList());
		}
	}
}
