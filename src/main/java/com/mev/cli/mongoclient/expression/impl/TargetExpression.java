package com.mev.cli.mongoclient.expression.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.expression.Expression;
import com.mev.cli.mongoclient.util.ExpressionUtil;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

@Component("targetExpression")
public class TargetExpression extends Expression<String> {
	private static final int FIRST_TABLE_OCCURRENCE_POINTER = 0;

	@Override
	public String interpret(Statement statement) {
		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
			
			return tableList.get(FIRST_TABLE_OCCURRENCE_POINTER);
		} else {
			return ExpressionUtil.NO_TABLE_NAME;
		}
	}

}
