/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class SortingRule.
 */
public class SortingRule {
	
	/** The name of column */
	public String name;
	
	/** The direction of sorting */
	public String direction;
	
	/**
	 * Instantiates a new sorting rule.
	 * 
	 * @param name the name of column
	 * @param direction the direction of sorting
	 */
	public SortingRule(String name, String direction){
		if (direction.equals(""))
			direction="ASC";
		else 
			direction = direction.toLowerCase().equals("asc")?"ASC":"DESC";
		
		this.name=name;
		this.direction=direction;
	}
	
	/**
	 * Converts to SQL
	 * 
	 * @return the SQL string
	 */
	public String to_sql(){
		return name+" "+direction;
	}
}
