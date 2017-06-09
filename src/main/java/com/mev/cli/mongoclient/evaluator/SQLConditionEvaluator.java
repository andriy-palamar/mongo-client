package com.mev.cli.mongoclient.evaluator;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;
import com.mev.cli.mongoclient.enums.SQLKeywordEnum;
import com.mev.cli.mongoclient.exception.TextProcessorFactoryException;
import com.mev.cli.mongoclient.exception.UnsupportedTextProcessorException;
import com.mev.cli.mongoclient.factory.TextProcessorFactory;

@Component
public class SQLConditionEvaluator extends AbstractEvaluator<Criteria> {
	final static Operator AND = new Operator(SQLKeywordEnum.AND.toString(), 2, Operator.Associativity.LEFT, 2);
	final static Operator OR = new Operator(SQLKeywordEnum.OR.toString(), 2, Operator.Associativity.LEFT, 1);

	private static final Parameters PARAMETERS;

	private TextProcessorFactory<Criteria> sqlConditionTextProcessorFactory;

	static {
		PARAMETERS = new Parameters();
		PARAMETERS.add(AND);
		PARAMETERS.add(OR);
		PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
	}

	@Autowired
	public SQLConditionEvaluator(
			@Qualifier("sqlConditionTextProcessorFactory") TextProcessorFactory<Criteria> sqlConditionTextProcessorFactory) {
		super(PARAMETERS);
		this.sqlConditionTextProcessorFactory = sqlConditionTextProcessorFactory;
	}

	@Override
	protected Criteria toValue(String arg0, Object arg1) {
		try {
			return sqlConditionTextProcessorFactory.getObjectByQuery(arg0);
		} catch (TextProcessorFactoryException e) {
			e.printStackTrace();
		} catch (UnsupportedTextProcessorException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected Criteria evaluate(Operator operator, Iterator<Criteria> operands, Object evaluationContext) {
		Criteria o1 = operands.next();
		Criteria o2 = operands.next();

		Criteria criteria = new Criteria();
		if (operator == OR) {
			criteria.orOperator(o1, o2);
		} else if (operator == AND) {
			criteria.andOperator(o1, o2);
		} else {
			throw new IllegalArgumentException();
		}

		return criteria;
	}
}
