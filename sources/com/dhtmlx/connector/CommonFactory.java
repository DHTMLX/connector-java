/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

 
/**
 * A factory for creating Data related objects.
 */
public class CommonFactory extends BaseFactory {
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	public DataItem createDataItem(HashMap<String,String> data, DataConfig config, int index){
		return new CommonDataItem(data,config,index);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataProcessor(com.dhtmlx.connector.BaseConnector, com.dhtmlx.connector.DataConfig, com.dhtmlx.connector.DataRequest, com.dhtmlx.connector.BaseFactory)
	 */
	public DataProcessor createDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory) {
		// TODO Auto-generated method stub
		return new CommonDataProcessor(connector, config, request, cfactory);
	}
}
