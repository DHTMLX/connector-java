/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * The Class AccessManager.
 * 
 * This class used to define which operation can be performed by connector.
 * By default - all operations are allowed 
 */
public class AccessManager {
	
	/** The set of access rules. */
	private HashMap<OperationType,Boolean> rules;
	
	/**
	 * Instantiates a new access manager.
	 */
	public AccessManager(){
		rules = new HashMap<OperationType, Boolean>();
	}
	
	/**
	 * Check if defined access mode is allowed 
	 * 
	 * @param mode access mode
	 * 
	 * @return true, if mode is allowed, false otherwise
	 */
	public boolean check(OperationType mode) {
		Boolean test = rules.get(mode);
		if (test == null) return true;
		return test.booleanValue();
	}
	
	/**
	 * Mark access mode as allowed
	 * 
	 * @param mode access mode
	 */
	public void allow(OperationType mode){
		rules.put(mode, true);
	}
	
	/**
	 * Mark access mode as denied
	 * 
	 * @param mode access mode
	 */
	public void deny(OperationType mode){
		rules.put(mode, false);
	}
	
	/**
	 * Deny all known access modes ( read, insert, update, delete ) 
	 */
	public void deny_all(){
		rules.put(OperationType.Read, false);
		rules.put(OperationType.Insert, false);
		rules.put(OperationType.Update, false);
		rules.put(OperationType.Delete, false);
	}
}
