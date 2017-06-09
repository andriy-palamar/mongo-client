package com.mev.cli.mongoclient.enums;

public enum SQLKeywordEnum {
	SELECT("SELECT"), FROM("FROM"),
	WHERE("WHERE"), AND("AND"), OR("OR"),
	GROUP_BY("GROUP BY"), ORDER_BY("ORDER BY"),
	ASC("ASC"), DESC("DESC"),
	OFFSET("OFFSET"), LIMIT("LIMIT");
	
	SQLKeywordEnum(String value) {
		this.value = value;
	}

	private String value;
	
	public String getValue() {
		return value;
	}
}
