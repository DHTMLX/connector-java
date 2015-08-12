/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Class PGSQLDBDataWrapper.
 */
public class PGSQLDBDataWrapper extends DBDataWrapper {

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
	public String get_new_id(ConnectorResultSet result)
			throws ConnectorOperationException {
		return query("SELECT LASTVAL() AS dhx_id").get("dhx_id");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#select_query(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected String select_query(String select, String from, String where,
			String sort, String start, String count) {
		
		String sql="SELECT "+select+" FROM "+from;
		if (!where.equals("")) sql+=" WHERE "+where;
		if (!sort.equals("")) sql+=" ORDER BY "+sort;
		if (!start.equals("") || !count.equals("")) sql+=" OFFSET "+start+" LIMIT "+count;
		return sql;
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#getStatement()
	 */
	@Override
	protected Statement getStatement() throws SQLException {
		return this.get_connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
}
