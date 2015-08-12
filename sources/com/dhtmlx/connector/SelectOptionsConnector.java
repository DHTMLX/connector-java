/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;
import java.sql.Connection;

/**
 * The Class OptionsConnector.
 * 
 * Fetch list of options to be used in complex components
 */
public class SelectOptionsConnector extends BaseConnector {
	
	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 */
	public SelectOptionsConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public SelectOptionsConnector(Connection db, DBType db_type){
		this(db,db_type, new SelectOptionsFactory());
	}
	
	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory
	 */
	public SelectOptionsConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}
}
