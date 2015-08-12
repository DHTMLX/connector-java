/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * The Class ConnectorResultSet.
 * 
 * Wrapper around result set , which allows to use it with different data wrappers
 */
public class ConnectorResultSet {
	
	/** The result */
	private ResultSet result=null;
	private Statement statement=null;
	
	/**
	 * Instantiates a new connector result set.
	 * 
	 * @param external_result native result set object
	 */
	public ConnectorResultSet(ResultSet external_result, Statement st){
		result = external_result;
		statement = st;
	}
	
	/**
	 * Gets the id of previously inserted record
	 * 
	 * @return the id of previously inserted record
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public String get_last_id() throws ConnectorOperationException{
		try {
			if (result!=null)
				return result.getString(1);
			return null;
		} catch (SQLException e) {
			throw new ConnectorOperationException(e.getMessage());
		}
	}
	
	/**
	 * Gets the next record in result set
	 * 
	 * @return the hash of data for the next record in result set
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public HashMap<String,String> get_next() throws ConnectorOperationException{
		try {
			if (result!=null){
				HashMap<String,String> data = new HashMap<String,String>();
					
				ResultSetMetaData rd = result.getMetaData();
				int max = rd.getColumnCount();
				
				for (int i=1; i<=max; i++)
					data.put(rd.getColumnLabel(i), result.getString(i));
				
				if (!result.next()){
					result.close();
					result = null;
				} 
				
				return data;
			}
			return null;
			
		} catch (SQLException e) {
			throw new ConnectorOperationException(e.getMessage());
		}
		
	}
	
	/**
	 * Gets the named field from top record in result set
	 * 
	 * @param name the name of field in question
	 * 
	 * @return value of named field as the string
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public String get(String name) throws ConnectorOperationException{
		String label=null;
		try{
			if (result!=null)
				label = result.getString(name);
			/*
		} catch(SQLException e){
			try{
				//check for different case was important in case of MySQL vs Oracle in php version
				//may be not necessary for JDBC
				label = result.getString(name.toLowerCase());*/ 	
		} catch(SQLException e){
			throw new ConnectorOperationException(e.getMessage());
		}
		
		return label;
	}
	
	/**
	 * change pointer of result set to specified position
	 * 
	 * @param start_from the new pointer position
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public void jump_to(int start_from) throws ConnectorOperationException {
		try {
			if (result!=null)
				result.absolute(start_from+1);
		} catch (SQLException e) {
			throw new ConnectorOperationException(e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (statement!=null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
