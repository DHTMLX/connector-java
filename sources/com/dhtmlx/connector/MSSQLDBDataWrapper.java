/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Class MSSQLDBDataWrapper.
 */
public class MSSQLDBDataWrapper extends DBDataWrapper {
	
	/** The start index */
	private int start_from;
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#escape(java.lang.String)
	 */
	@Override
	public String escape(String data) {
		return data.replace ("\\","\\\\'").replace("'","''");
	}

	

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#get_new_id(com.dhtmlx.connector.ConnectorResultSet)
	 */
	@Override
	public String get_new_id(ConnectorResultSet result) throws ConnectorOperationException {
		return query("SELECT @@IDENTITY AS dhx_id").get("dhx_id");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#select_query(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected String select_query(String select, String from, String where,
			String sort, String start, String count) {
		if (count.equals(""))
			return super.select_query(select, from, where, sort, start, count);
		
		String sql="SELECT ";
		if (!count.equals(""))
			sql+=" TOP "+Integer.toString((Integer.parseInt(count)+Integer.parseInt(start)));
		sql+=" "+select+" FROM "+from;
		if (!where.equals("")) sql+=" WHERE "+where;
		if (!sort.equals("")) sql+=" ORDER BY "+sort;
		
		if (!start.equals("")) 
			start_from=Integer.parseInt(start);
		else 
			start_from=0;

		return sql;
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#getStatement()
	 */
	@Override
	protected Statement getStatement() throws SQLException {
		return this.get_connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#select(com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public ConnectorResultSet select(DataRequest source)
			throws ConnectorOperationException {
		ConnectorResultSet temp = super.select(source);
		temp.jump_to(start_from);
		return temp;
	}

	
}
