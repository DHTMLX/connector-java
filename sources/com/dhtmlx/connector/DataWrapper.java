/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * The Class DataWrapper.
 * 
 * Abstract data access class. Provides base methods for CRUD operations.
 */
public abstract class DataWrapper{
	
	/** The data config */
	protected DataConfig config;
	
	/** The connection */
	protected Object connection;
	
	/** The transaction type */
	private TransactionType transaction_type = TransactionType.NONE;
	//protected Connection connection; - not used because of strict typing
	
	/**
	 * Inits self
	 * 
	 * @param connection the connection
	 * @param external_config the data config
	 */
	public void init(Object connection, DataConfig external_config){
		this.connection = connection;
		config = external_config;
	}
	
	/**
	 * Insert data in storage
	 * 
	 * @param data the data wrapped in DataAction object
	 * @param source the source defined by DataRequest object
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract void insert(DataAction data, DataRequest source) throws ConnectorOperationException;
	
	/**
	 * Delete data from storage
	 * 
	 * @param data the data wrapped in DataAction object
	 * @param source the source defined by DataRequest object
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract void delete(DataAction data, DataRequest source) throws ConnectorOperationException;
	
	/**
	 * Update data in storage
	 * 
	 * @param data the data wrapped in DataAction object
	 * @param source the source defined by DataRequest object
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract void update(DataAction data, DataRequest source) throws ConnectorOperationException;
	
	/**
	 * Select data from storage
	 * 
	 * @param source the source defined by DataRequest object
	 * 
	 * @return the connector result set
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract ConnectorResultSet select(DataRequest source) throws ConnectorOperationException;
	
	/**
	 * Gets the size of the data in storage.
	 * 
	 * @param source the source defined by DataRequest object
	 * 
	 * @return the size of collection in storage
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract String get_size(DataRequest source) throws ConnectorOperationException;
	
	/**
	 * Gets the all variations of defined field in the storage
	 * 
	 * @param source the source defined by DataRequest object
	 * 
	 * @return the set of variations
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract ConnectorResultSet get_variants(DataRequest source, String field) throws ConnectorOperationException;
	
	/**
	 * Gets the sql string for named operation
	 * 
	 * @param name the name of operation
	 * @param data the hash of data, will be used to fill vars in sql  
	 * 
	 * @return the sql string
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public String get_sql(OperationType name, HashMap<String,String> data) throws ConnectorConfigException{
		return "";
	}
	
	
	/**
	 * Begin transaction.
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public void begin_transaction() throws ConnectorConfigException, ConnectorOperationException{
		throw new ConnectorConfigException("Data wrapper not supports transactions.");
	}
	
	/**
	 * Commit transaction.
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public void commit_transaction() throws ConnectorConfigException, ConnectorOperationException{
		throw new ConnectorConfigException("Data wrapper not supports transactions.");
	}
	
	/**
	 * Rollback transaction.
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public void rollback_transaction() throws ConnectorConfigException, ConnectorOperationException{
		throw new ConnectorConfigException("Data wrapper not supports transactions.");
	}	
	
	/**
	 * Sets the transaction mode.
	 * 
	 * @param mode the new _transaction_mode
	 */
	public void set_transaction_mode(TransactionType mode){
		transaction_type = mode;
	}
	
	/**
	 * Checks if current mode is "global transaction"
	 * 
	 * @return true, if "global transaction" flag is set
	 */
	public boolean is_global_transaction(){
		return transaction_type == TransactionType.GLOBAL;
	}
	
	/**
	 * Checks if current mode is "record transaction"
	 * 
	 * @return true, if "record transaction" flag is set
	 */
	public boolean is_record_transaction(){
		return transaction_type == TransactionType.OPERATION;
	}
}
