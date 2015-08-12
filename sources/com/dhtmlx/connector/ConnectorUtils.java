/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Class ConnectorUtils.
 * 
 * Common operations, which missed in base Java classes
 */
public class ConnectorUtils {
	
	/**
	 * Join array in to the string
	 * 
	 * @param strings the array of a strings
	 * @param separator the separator string
	 * 
	 * @return the result string
	 */
	public static String join(Object[] strings, String separator){
	    StringBuffer sb = new StringBuffer();
	    for (int i=0; i < strings.length; i++) {
	        if (i != 0) sb.append(separator);
	  	    sb.append(strings[i].toString());
	  	}
	  	return sb.toString();
	}
}
