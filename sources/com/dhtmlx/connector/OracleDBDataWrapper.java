/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;



/**
 * The Class OracleDBDataWrapper.
 */
public class OracleDBDataWrapper extends DBDataWrapper {
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#escape(java.lang.String)
	 */
	@Override
	public String escape(String data) {
		return data.replace ("\\","\\\\'").replace("'","\\'");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#getStatement()
	 */
	@Override
	protected Statement getStatement() throws SQLException {
		return this.get_connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	
	
	/**
	 * Builds insert query, specific for MSSQL
	 * 
	 * @param data the data
	 * 
	 * @return the sql string
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public String insert_query(String data)
			throws ConnectorOperationException {
		try {
			
			CallableStatement cs = this.get_connection().prepareCall(data);
			cs.registerOutParameter(1,java.sql.Types.INTEGER);
			cs.execute();
			
			return Integer.toString(cs.getInt(1));
		}
		catch (SQLException e){
			throw new ConnectorOperationException("Invalid SQL: "+data+"\n"+e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#insert(com.dhtmlx.connector.DataAction, com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public void insert(DataAction data, DataRequest source) throws ConnectorOperationException{
		String sql = insert_query(data,source);
		data.success(insert_query(sql));
	}

	//this code may break transactions logic
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#insert_query(com.dhtmlx.connector.DataAction, com.dhtmlx.connector.DataRequest)
	 */
	@Override
	protected String insert_query(DataAction data, DataRequest source) {
		return "BEGIN "+super.insert_query(data, source)+" returning "+config.id.db_name+" into ?; END;";
	}


	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#get_new_id(com.dhtmlx.connector.ConnectorResultSet)
	 */
	@Override
	public String get_new_id(ConnectorResultSet result) throws ConnectorOperationException{
		if (!sequence_name.equals(""))
			return this.query("SELECT "+Pattern.compile("nextval", Pattern.CASE_INSENSITIVE).matcher(sequence_name).replaceAll("CURRVAL")+" as dhx_id FROM DUAL").get("dhx_id");
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DBDataWrapper#select_query(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	protected String select_query(String select, String from, String where,
			String sort, String start, String count) {
		String sql = "SELECT "+select+" FROM "+from;
		if (!where.equals("")) sql+=" WHERE "+where;
		if (!sort.equals("")) sql+=" ORDER BY "+sort;
		if (!start.equals("")|| !count.equals("")){
			String end = Integer.toString(Integer.parseInt(count)+Integer.parseInt(start));
			sql="SELECT * FROM ( select /*+ FIRST_ROWS("+count+")*/dhx_table.*, ROWNUM rnum FROM ("+sql+") dhx_table where ROWNUM <= "+end+" ) where rnum >"+start;
		}
		return sql;
	}

}
