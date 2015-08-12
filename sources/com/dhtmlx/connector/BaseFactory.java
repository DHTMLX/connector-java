/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * A factory for creating objects, used by connector
 */
public class BaseFactory {
	
	/**
	 * Creates a new DataItem object.
	 * 
	 * Each component redefines this method, to create component specific data items
	 * 
	 * @param data the hash of data
	 * @param config the data config
	 * @param index the index of data in resultset
	 * 
	 * @return the data item
	 */
	public DataItem createDataItem(HashMap<String,String> data, DataConfig config, int index){
		return new DataItem(data,config,index);
	}
	
	/**
	 * Creates a new DataWrapper object.
	 * 
	 * Creates a MySQL data wrapper by default
	 * 
	 * @return the data wrapper
	 */
	public DataWrapper createDataWrapper(){
		return new MySQLDBDataWrapper();
	}
	
	/**
	 * Creates a new DataProcessor object.
	 * 
	 * Each component redefines this method, to create component specific data processor
	 * 
	 * @param connector the connector
	 * @param config the data config
	 * @param request the data request
	 * @param cfactory the class factory, which will be used by result data processor
	 * 
	 * @return the data processor
	 */
	public DataProcessor createDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory) {
		return new DataProcessor(connector, config, request, cfactory);
	}
	
	/**
	 * Creates a new DataAction object.
	 * 
	 * @param status the status of operation
	 * @param id the id of record
	 * @param item_data the hash of data
	 * 
	 * @return the data action
	 */
	public DataAction createDataAction(String status, String id,
			HashMap<String, String> item_data) {
		return new DataAction(status, id, item_data);
	}
	
	public RenderStrategy createRenderStrategy() {
		return new RenderStrategy();
	}
}
