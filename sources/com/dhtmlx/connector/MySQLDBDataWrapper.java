/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

// TODO: Auto-generated Javadoc
/**
 * The Class MySQLDBDataWrapper.
 */
public class MySQLDBDataWrapper extends DBDataWrapper {
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#escape(java.lang.String)
	 */
	@Override
	public String escape(String data) {
		return data.replace ("\\","\\\\'").replace("'","\\'");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#get_new_id(com.dhtmlx.connector.ConnectorResultSet)
	 */
	@Override
	public String get_new_id(ConnectorResultSet result) throws ConnectorOperationException {
		return query("SELECT LAST_INSERT_ID() as new_id").get("new_id");
	}
}
