/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class DummyStringConnector.
 * 
 * Instead of executing any DB queries, connector will otput the XML string, 
 * which was defined during creation. It is used to define list of options through API,
 * instead of polling them from DB
 */
public class DummyStringConnector extends BaseConnector {
	
	/** The xml data. */
	private String ready_data;
	
	/**
	 * Instantiates a new dummy string connector.
	 * 
	 * @param data the xml data
	 */
	public DummyStringConnector(String data) {
		super(null);
		ready_data = data;
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render()
	 */
	@Override
	public String render() {
		return ready_data;
	}
	

}
