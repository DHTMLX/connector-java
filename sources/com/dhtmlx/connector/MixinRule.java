package com.dhtmlx.connector;

import java.util.HashMap;

public class MixinRule {

	protected String field;
	protected String value = "";
	protected BaseConnector conn = null;
	protected RelationSet filter;

	public MixinRule(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public MixinRule(String field, BaseConnector conn, RelationSet filter) {
		this.field = field;
		this.conn = conn;
		this.filter = filter;
	}

	public String getName() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public BaseConnector getConnector() {
		return conn;
	}
	
	public RelationSet get_filter() {
		return filter;
	}
}
