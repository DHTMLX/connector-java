/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class ConnectorOperationException.
 * 
 * Exception occurs when some DB error occurs during connector logic processing
 */
public class ConnectorOperationException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7170611402641940431L;

	/**
	 * Instantiates a new connector operation exception.
	 * 
	 * @param message the message
	 */
	public ConnectorOperationException(String message){
		super(message);
	}
}
