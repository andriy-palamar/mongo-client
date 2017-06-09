package com.mev.cli.mongoclient.validator.impl;

import java.io.StringReader;

import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.validator.SQLValidator;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

@Component
public class SelectSQLValidator implements SQLValidator {

	@Override
	public boolean isValid(String query) {
		JSqlParser sqlParser = new CCJSqlParserManager();
		try {
			Statement statement = sqlParser.parse(new StringReader(query));

			return statement instanceof Select;
		} catch (JSQLParserException e) {
			return false;
		}
	}

}
