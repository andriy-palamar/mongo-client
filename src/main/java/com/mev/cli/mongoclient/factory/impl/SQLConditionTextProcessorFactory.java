package com.mev.cli.mongoclient.factory.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;

@Component("sqlConditionTextProcessorFactory")
public class SQLConditionTextProcessorFactory implements TextProcessorFactory<Criteria> {
	private static final String EQUALS = "=";
	private static final String NOT_EQUALS1 = "<>";
	private static final String NOT_EQUALS2 = "!=";
	private static final String GREATER = ">";
	private static final String LESS = "<";
	private static final String GREATER_EQUALS = ">=";
	private static final String LESS_EQUALS = "<=";

	private static final String SPLIT_QUERY;

	static {
		SPLIT_QUERY = GREATER_EQUALS + "|" + LESS_EQUALS + "|" + EQUALS + "|" + NOT_EQUALS1 + "|" + NOT_EQUALS2 + "|"
				+ GREATER + "|" + LESS;
	}

	@Override
	public Criteria getObjectByQuery(String query)
			throws TextProcessorFactoryException {
		Optional.ofNullable(query).orElseThrow(TextProcessorFactoryException::new);

		String[] values;
		try {
			values = query.split(SPLIT_QUERY);

			values[0] = values[0].trim();
			values[1] = values[1].trim().replaceAll("\"|'", "");
		} catch(Exception e) {
			throw new TextProcessorFactoryException();
		}
		
		Object typedValue = getTypedValue(values[1]);

		if (query.contains(GREATER_EQUALS)) {
			return Criteria.where(values[0]).gte(typedValue);
		} else if (query.contains(LESS_EQUALS)) {
			return Criteria.where(values[0]).lte(typedValue);
		} else if (query.contains(NOT_EQUALS1) || query.contains(NOT_EQUALS2)) {
			return Criteria.where(values[0]).ne(typedValue);
		} else if (query.contains(EQUALS)) {
			return Criteria.where(values[0]).is(typedValue);
		} else if (query.contains(GREATER)) {
			return Criteria.where(values[0]).gt(typedValue);
		} else if (query.contains(LESS)) {
			return Criteria.where(values[0]).lt(typedValue);
		} else {
			throw new TextProcessorFactoryException("Cannot parse: " + query);
		}
	}
	
	private Object getTypedValue(String value) {
		if (StringUtils.isNumeric(value)) {
			return Double.parseDouble(value);
		} else {
			return value;
		}
	}

}
