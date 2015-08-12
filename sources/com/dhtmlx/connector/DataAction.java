/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Class DataAction.
 * 
 * Class wraps DB operation. It contains all related data from incoming request 
 * and status of operation, after its execution.
 * 
 */
public class DataAction{
	
	/** The status of operation */
	private String status;
	
	/** The id of record */
	private String id;
	
	/** The new id of record */
	private String nid;
	
	/** The data of record */
	private HashMap<String,String> data;
	
	/** The attributes of response */
	private HashMap<String,String> attrs;
	
	/** The output buffer */
	private String output;
	
	/** Flag of operation complete */
	private boolean ready;
	
	/** The array of additional fields */
	private ArrayList<String> addf;
	
	/** The array of fields to be ignored */
	private ArrayList<String> delf;

	/**
	 * Instantiates a new data action.
	 * 
	 * @param status the status of action
	 * @param id the id of record
	 * @param data the data hash of record
	 */
	public DataAction(String status, String id, HashMap<String,String> data){
		this.status = status;
		this.id = id;
		this.nid = id;
		this.data = data;

		attrs = new HashMap<String, String>();
		ready = false;
		
		addf = new ArrayList<String>();
		delf = new ArrayList<String>();
	}

	/**
	 * Add new field
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void add_field(String name, String value){
		data.put(name, value);
		addf.add(name);
	}
	
	/**
	 * Remove existing field.
	 * 
	 * @param name the name
	 */
	public void remove_field(String name){
		delf.add(name);
	}
	
	/**
	 * Sync config between data action and data config object
	 * 
	 * @param config the data config
	 * 
	 * @throws ConnectorConfigException the connector configuration exception
	 */
	public void sync_config(DataConfig config) throws ConnectorConfigException{
		for (int i=0; i<addf.size(); i++)
			config.add_field(addf.get(i));
		for (int i=0; i<delf.size(); i++)
			config.remove_field(delf.get(i));
	}

	/**
	 * Gets the value of named field
	 * 
	 * @param name the name of field
	 * 
	 * @return the value of field
	 */
	public String get_value(String name) {
		String value = data.get(name);
		return value;
	}
	
	/**
	 * Sets value of named field 
	 * 
	 * @param name the name of field
	 * @param value the value of field
	 * 
	 * @return the previous value 
	 */
	public String set_value(String name, String value) {
		return data.put(name, value);
	}

	/**
	 * Gets the id of the record
	 * 
	 * @return the id of the record
	 */
	public String get_id() {
		return id;
	}
	
	/**
	 * Gets the data hash of the record
	 * 
	 * @return the data hash of the record
	 */
	public HashMap<String, String> get_data(){
		return data;
	}
	
	/**
	 * Gets the _userdata_value.
	 * 
	 * @param name the name
	 * 
	 * @return the _userdata_value
	 */
	public String get_userdata_value(String name){
		return get_value(name);
	}
	
	/**
	 * Set extra data value.
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void set_userdata_value(String name, String value){
		set_value(name,value); 
	}
	
	/**
	 * Gets the status of action
	 * 
	 * @return the status of action
	 */
	public String get_status(){
		return status;
	}

	/**
	 * Sets the response text.
	 * 
	 * @param text the new response text
	 */
	public void set_response_text(String text){
		set_response_xml("<![CDATA["+text+"]]>");
	}
	
	/**
	 * Sets the response xml.
	 * 
	 * @param xml_text the new response text as xml string
	 */
	public void set_response_xml(String xml_text){
		output=xml_text;
	}

	/*! set id
    @param  id
        id value
	*/
	public void set_id(String id) {
		this.id = id;
		LogManager.getInstance().log("Change id: "+id);
	}

	/*! set new id
    @param  id
        id value
	*/
	public void set_new_id(String nid) {
		this.nid = nid;
		LogManager.getInstance().log("Change new id: "+nid);
	}
	
	/**
	 * Sets an attribute of the response
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void set_response_attributes(String name, String value){
		attrs.put(name,value);
	}
	
	/**
	 * Checks if is operation is complete
	 * 
	 * @return true, if is complete
	 */
	public boolean is_ready(){
		return ready;
	}
	
	/**
	 * Gets the new id
	 * 
	 * New Id may differ from original one in case of Insert operation
	 * 
	 * @return the new id of the record
	 */
	public String get_new_id(){
		return nid;
	}
	
	/**
	 * Mark operation as incorrect
	 */
	public void error(){
		status = "error";
		ready = true;
	}
	
	/**
	 * Mark operation as invalid 
	 */
	public void invalid(){
		status = "invalid";
		ready = true;
	}

	/**
	 * Mark operation as successfully completed
	 * 
	 * @param new_id the new id
	 */
	public void success(String new_id) {
		if (new_id != null)
			nid = new_id;
		success();
	}

	/**
	 * Mark operation as successfully completed
	 */
	public void success() {
		ready=true;
	}
	
	/**
	 * Convert data action to xml string
	 * 
	 * @return the string
	 */
	public String to_xml(){
		StringBuffer out = new StringBuffer();
		
		out.append("<action type='"); out.append(status); out.append("' sid='"); 
		out.append(id); out.append("' tid='"); out.append(nid); out.append("' ");
		
		Iterator<String> it = attrs.keySet().iterator(); 
		while (it.hasNext()){
			String key = it.next();
			out.append(key); out.append("='"); out.append(attrs.get(key)); out.append("' ");
		}
		
		out.append(">"); 
		if (output!=null)
			out.append(output);
		out.append("</action>");
		
		return out.toString();
	}

	/**
	 * Set current status of the action
	 * 
	 * @param status the new status
	 */
	public void set_status(String status) {
		this.status = status;
	}
}
