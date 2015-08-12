/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class ConnectorField.
 * 
 * It is a Wrapper around structure, which holds field name and field alias
 */
public class ConnectorField {
	
	/** The name of field */
	public String name;
	
	/** The db's name of field */
	public String db_name;
	
	/**
	 * Instantiates a new connector field.
	 * 
	 * Default constructor sets both name and alias as empty ones.
	 */
	public ConnectorField(){
		this("","");
	}
	
	/**
	 * Instantiates a new connector field.
	 * 
	 * @param db_name - name of field in DB 
	 */
	public ConnectorField(String db_name){
		this(db_name,db_name);
	}

	/**
	 * Instantiates a new connector field.
	 * 
	 * @param db_name name of the field in a DB
	 * @param name alias of the field
	 */
	public ConnectorField(String db_name, String name) {
		this.name = name;
		this.db_name = db_name;
	}
	
	/**
	 * Checks if current field is empty (was not defined)
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty(){
		return db_name.equals("");
	}
}
