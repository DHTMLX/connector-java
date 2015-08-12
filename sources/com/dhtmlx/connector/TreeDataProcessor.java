/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class TreeDataProcessor.
 */
public class TreeDataProcessor extends DataProcessor {
	
	/**
	 * Instantiates a new tree data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public TreeDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
		request.set_relation("");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataProcessor#name_data(java.lang.String)
	 */
	@Override
	public String name_data(String name) {
		if (name.equals("tr_pid"))
			return config.relation_id.name;
		if (name.equals("tr_text"))
			return config.text.get(0).name;
		return super.name_data(name);
	}
}
