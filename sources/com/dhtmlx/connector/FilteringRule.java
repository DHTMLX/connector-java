/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class FilteringRule.
 * 
 * Storage wrapper for filtering rules 
 */
public class FilteringRule {
	
	/** The name of field. */
	public String name;
	
	/** The operation. */
	public String operation;
	
	/** The value. */
	public String value;
	
	/** The rule as sql sting . */
	public String sql;
	
	/**
	 * Instantiates a new filtering rule.
	 * 
	 * @param sql the rule as sql string
	 */
	public FilteringRule(String sql){
		this.sql = sql;
	}
	
	/**
	 * Instantiates a new filtering rule.
	 * 
	 * @param name the name field
	 * @param value the value
	 */
	public FilteringRule(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Instantiates a new filtering rule.
	 * 
	 * @param name the name of field
	 * @param value the value
	 * @param operation the operation
	 */
	public FilteringRule(String name, String value, String operation){
		this.name = name;
		this.value = value;
		this.operation = operation;
	}
	
	/**
	 * To_sql.
	 * 
	 * @param db the DataWrapper object
	 * 
	 * @return the string
	 */
	public String to_sql(DBDataWrapper db){
		if (sql!=null && !sql.equals("")) return sql;
		if (operation!=null && !operation.equals("")) return name+" "+operation+" '"+db.escape(value)+"'";
		return name+" LIKE '%"+db.escape(value)+"%'";
	}
}
