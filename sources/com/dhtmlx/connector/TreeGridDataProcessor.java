/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class TreeGridDataProcessor.
 */
public class TreeGridDataProcessor extends GridDataProcessor {
	
	/**
	 * Instantiates a new tree grid data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public TreeGridDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
		request.set_relation("");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.GridDataProcessor#name_data(java.lang.String)
	 */
	@Override
	public String name_data(String name) {
		if (name.equals("gr_pid"))
			return config.relation_id.name;
		return super.name_data(name);
	}
}
