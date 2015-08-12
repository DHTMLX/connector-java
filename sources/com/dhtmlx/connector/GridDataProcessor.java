/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class GridDataProcessor.
 */
public class GridDataProcessor extends DataProcessor {
	
	/**
	 * Instantiates a new grid data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public GridDataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataProcessor#name_data(java.lang.String)
	 */
	public String name_data(String name){
		if (name.equals("gr_id")) return config.id.name;
		String [] parts = name.split("c");
		
		
		try {
			if (parts[0].equals("")){
				int index=Integer.parseInt(parts[1]);
				if (config.text.size() <= index) return "";
				return config.text.get(index).name;
			}
		} catch (NumberFormatException e) { }
			
		return name;
	}
}
