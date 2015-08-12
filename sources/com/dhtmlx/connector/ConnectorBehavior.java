/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Class ConnectorBehavior.
 * 
 * Class represents the system of server side events, which can be used to configure 
 * how connector must process select and update requests. 
 */
public class ConnectorBehavior {
	
	/** The master behavior instance */
	private ConnectorBehavior instance;
	
	/**
	 * Instantiates a new connector behavior.
	 */
	public ConnectorBehavior(){
		instance = null;
		//instance = this;
	}
	
	/**
	 * Attach new behavior
	 * 
	 * @param custom the custom behavior
	 */
	public void attach(ConnectorBehavior custom){
		if (instance!=null)
			instance.attach(custom);
		else
			instance=custom;
	}
	
	/**
	 * Trigger event
	 * 
	 * @return the active behavior
	 */
	public ConnectorBehavior trigger(){
		return this;
		//return instance;
	}
	
	//event handlers below
	
	/**
	 * Before sort event
	 * 
	 * Occurs in selection mode, when incoming request parsed and before data selection from DB
	 */
	public void beforeSort(ArrayList<SortingRule> sorters){
		if (instance!=null)
			instance.beforeSort(sorters);
	}
	
	/**
	 * Before filter event
	 * 
	 * Occurs in selection mode, when incoming request parsed and before data selection from DB
	 */
	public void beforeFilter(ArrayList<FilteringRule> filters){
		if (instance!=null)
			instance.beforeFilter(filters);
	}

	/**
	 * Before render event
	 * 
	 * Occurs in selection mode. Event logic called for rendering of each item. 
	 * Related data item is provided as parameter of the called method. 
	 * 
	 * @param data the data item
	 */
	public void beforeRender(DataItem data) {
		if (instance!=null)
			instance.beforeRender(data);
	}

	/**
	 * Before processing event
	 * 
	 * Occurs in update mode, before execution any DB operations.
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void beforeProcessing(DataAction action) {
		if (instance!=null)
			instance.beforeProcessing(action);
	}
	

	/**
	 * After DB error event
	 * 
	 * Occurs in update mode, after some DB error
	 * Related error object is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void afterDBError(DataAction action, Throwable e) {
		if (instance!=null)
			instance.afterDBError(action, e);
	}	

	/**
	 * After processing event
	 * 
	 * Occurs in update mode, after execution any DB operations.
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method.
	 * 
	 * @param action the data action
	 */
	public void afterProcessing(DataAction action) {
		if (instance!=null)
			instance.afterProcessing(action);
	}

	/**
	 * Before delete event
	 * 
	 * Occurs in update mode, before deleting record from DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void beforeDelete(DataAction action) {
		if (instance!=null)
			instance.beforeDelete(action);
	}

	/**
	 * Before insert event
	 * 
	 * Occurs in update mode, before inserting record in DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void beforeInsert(DataAction action) {
		if (instance!=null)
			instance.beforeInsert(action);
	}

	/**
	 * Before update event
	 * 
	 * Occurs in update mode, before updating record in DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void beforeUpdate(DataAction action) {
		if (instance!=null)
			instance.beforeUpdate(action);
	}

	/**
	 * After delete event
	 * 
	 * Occurs in update mode, after deleting record from DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action 
	 */
	public void afterDelete(DataAction action) {
		if (instance!=null)
			instance.afterDelete(action);
	}

	/**
	 * After insert event
	 * 
	 * Occurs in update mode, after inserting record in DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void afterInsert(DataAction action) {
		if (instance!=null)
			instance.afterInsert(action);
	}

	/**
	 * After update event
	 * 
	 * Occurs in update mode, after updating record in DB
	 * Event logic called for each updated record. 
	 * Related data action is provided as parameter of the called method. 
	 * 
	 * @param action the data action
	 */
	public void afterUpdate(DataAction action) {
		if (instance!=null)
			instance.afterUpdate(action);
	}

	/**
	 * Before output event
	 * 
	 * Event occurs before rendering output of connector. 
	 * It can be used to inject any extra data in the output
	 * 
	 * @param out xml string 
	 * @param http_request the http request
	 */
	public void beforeOutput(ConnectorOutputWriter out, HttpServletRequest http_request, HttpServletResponse http_response) {
		if (instance!=null)
			instance.beforeOutput(out, http_request, http_response);
	}

	public void beforeOrder(DataAction action) {
		if (instance!=null)
			instance.beforeOrder(action);
	}
	
	public void afterOrder(DataAction action) {
		if (instance!=null)
			instance.afterOrder(action);
	}

	
}
