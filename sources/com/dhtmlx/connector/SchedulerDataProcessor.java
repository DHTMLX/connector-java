/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class SchedulerDataProcessor.
 */
public class SchedulerDataProcessor extends DataProcessor {
	
	/**
	 * Instantiates a new scheduler data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public SchedulerDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataProcessor#name_data(java.lang.String)
	 */
	@Override
	protected String name_data(String name) {
		if (name.equals("start_date"))
			return config.text.get(0).name;
		if (name.equals("end_date"))
			return config.text.get(1).name;
		if (name.equals("text"))
			return config.text.get(2).name;
		
		return super.name_data(name);
	}
	
	
}
