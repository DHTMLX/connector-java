package com.dhtmlx.connector;

import java.sql.Connection;

public class FormConnector extends BaseConnector {
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 */
	public FormConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public FormConnector(Connection db, DBType db_type){
		this(db,db_type, new FormFactory());
	}
	
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public FormConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}
	
	
	@Override
	protected void parse_request(){
		super.parse_request();
		
		String id =  http_request.getParameter("id");
		if (id != null && !id.equals(""))
			request.set_filter(config.id.name, id, "=");
	}
}
