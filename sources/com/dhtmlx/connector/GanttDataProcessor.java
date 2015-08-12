/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class GanttDataProcessor.
 */
public class GanttDataProcessor extends DataProcessor {
	
	/**
	 * Instantiates a new gantt data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public GanttDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataProcessor#name_data(java.lang.String)
	 */
	@Override
	protected String name_data(String name) {
		if (name.equals("start_date"))
			return config.text.get(0).name;
		else if (name.equals("duration") && this.config.text.get(1).equals("duration"))
			return this.config.text.get(1).name;
		else if (name.equals("end_date") && this.config.text.get(1).equals("end_date"))
			return this.config.text.get(1).name;
		else if (name.equals("text"))
			return config.text.get(2).name;
		else if (name.equals("id"))
			return config.id.name;

		return super.name_data(name);
	}
	
	
}
