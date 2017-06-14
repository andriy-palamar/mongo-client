package com.mev.cli.mongoclient.validator.impl;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mev.cli.mongoclient.enums.SQLKeywordEnum;
import com.mev.cli.mongoclient.util.ExpressionUtil;
import com.mev.cli.mongoclient.validator.SQLValidator;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

@Component
public class SelectSQLValidator implements SQLValidator {

	private boolean valid;
	
	@Override
	public boolean isValid(String query) {
		valid = false;
		JSqlParser sqlParser = new CCJSqlParserManager();
		try {
			Statement statement = sqlParser.parse(new StringReader(query));
			
			if (statement instanceof Select) {
				valid = true;
				PlainSelect plainSelect = ExpressionUtil.getPlainSelect(statement);
				
				Optional.ofNullable(plainSelect.getLimit())
					.ifPresent(l -> valid = l.toString().contains(SQLKeywordEnum.LIMIT.toString()));
			}

			return valid;
		} catch (JSQLParserException e) {
			return valid;
		}
	}

}
