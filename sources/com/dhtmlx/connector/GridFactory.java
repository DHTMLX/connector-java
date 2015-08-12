/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

 
/**
 * A factory for creating Grid related objects.
 */
public class GridFactory extends BaseFactory {
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	public DataItem createDataItem(HashMap<String,String> data, DataConfig config, int index){
		return new GridDataItem(data,config,index);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataProcessor(com.dhtmlx.connector.BaseConnector, com.dhtmlx.connector.DataConfig, com.dhtmlx.connector.DataRequest, com.dhtmlx.connector.BaseFactory)
	 */
	public DataProcessor createDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory) {
		// TODO Auto-generated method stub
		return new GridDataProcessor(connector, config, request, cfactory);
	}
}
