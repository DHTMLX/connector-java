/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;

/**
 * The Class DistinctOptionsConnector.
 * 
 * This connecto is used to select list of unique values, 
 * and can be used to fill co|coro columns and select filters in grid
 */
public class DistinctOptionsConnector extends OptionsConnector {

	/**
	 * Instantiates a new distinct options connector.
	 * 
	 * @param db the db connection
	 */
	public DistinctOptionsConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new distinct options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public DistinctOptionsConnector(Connection db, DBType db_type){
		this(db,db_type, new BaseFactory());
	}
	
	/**
	 * Instantiates a new distinct options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory
	 */
	public DistinctOptionsConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.OptionsConnector#render()
	 */
	@Override
	public String render() {
		if (!init_flag){
			init_flag = true;
			return "";
		}
		try {
			ConnectorResultSet res = sql.get_variants(request, config.text.get(0).db_name);
			return render_set(res);
		} catch (ConnectorOperationException e) {
			return "";
		}
	}
}
