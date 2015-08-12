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
public class OptionsConnector extends BaseConnector {
	
	/** The init flag. */
	protected boolean init_flag = false;
	
	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 */
	public OptionsConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public OptionsConnector(Connection db, DBType db_type){
		this(db,db_type, new BaseFactory());
	}
	
	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory
	 */
	public OptionsConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render()
	 */
	@Override
	public String render(){
		if (!init_flag){
			init_flag = true;
			return "";
		}
		ConnectorResultSet res = get_resource();
		try {
			return super.render_set(res);
		} catch (ConnectorOperationException e) {
			return "";
		}
	}
	
	public void render_save(){
		try {
			config.remove_field(config.id.name);
		} catch (ConnectorConfigException e) {
			e.printStackTrace();
		}
		super.render();	}
}
