/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectorConfigException.
 * 
 * Exception occurs when configuration of connector contains logical problems.
 * - incorrect field names
 * - duplicate fields
 * - etc.
 *  
 */
public class ConnectorConfigException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4307479556310558135L;
	
	/**
	 * Instantiates a new connector config exception.
	 * 
	 * @param message the message
	 */
	public ConnectorConfigException(String message){
		super(message);
	}
}
