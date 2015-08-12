package com.dhtmlx.connector;

import java.sql.Connection;

public class KeyGridConnector extends GridConnector {
	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 */
	public KeyGridConnector(Connection db){
		this(db,DBType.Custom);
		
		this.event.attach(new KeyGridBehavior(this));
	}

	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public KeyGridConnector(Connection db, DBType db_type){
		this(db,db_type, new KeyGridFactory());
	}
	
	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public KeyGridConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}
}
