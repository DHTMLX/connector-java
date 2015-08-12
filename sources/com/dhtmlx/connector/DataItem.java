/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The Class DataItem.
 * 
 * Wrapper around the fetched data. Controls how the data transforms into the xml string. 
 */
public class DataItem implements DataItemInterface {
	
	/** The hash of data. */
	protected HashMap<String,String> data;
	
	/** The config. */
	protected DataConfig config;
	
	/** The index of record. */
	protected int index;
	
	/** The skip flag. */
	protected boolean skip;
	
	/** The kids flag */
	protected int kids = -1;
	
	protected HashMap<String,String> userdata = null;
	
	/**
	 * Instantiates a new data item.
	 * 
	 * @param data the hash of data
	 * @param config the config
	 * @param index the index of record
	 */
	public DataItem(HashMap<String,String> data, DataConfig config, int index){
		skip = false;
		this.index = index;
		this.data = data;
		this.config = config;
	}
	
	/**
	 * Gets the value of the field
	 * 
	 * @param name the name of the field
	 * 
	 * @return the value of the field
	 */
	public String get_value(String name){
		return data.get(name);
	}
	
	/**
	 * Sets value of the field
	 * 
	 * @param name the name of the field
	 * @param value the value of the field
	 */
	public void set_value(String name, String value){
		data.put(name, value);
	}
	
	/**
	 * Gets the id of the record
	 * 
	 * @return the id of the record
	 */
	public String get_id(){
		return get_value(config.id.name);
	}
	
	/**
	 * Sets the new id for the record
	 * 
	 * @param value the new id for the record
	 */
	public void set_id(String value){
		set_value(config.id.name, value);
	}
	
	/**
	 * Gets the index of the record
	 * 
	 * @return the index of the record
	 */
	public int get_index(){
		return index;
	}
	
	/**
	 * Gets the count of child items
	 * 
	 * @return the count of child items
	 */
	public int has_kids(){
		return kids;
	}
	
	/**
	 * Sets the count of child items
	 * 
	 * @param count the count of child items
	 */
	public void set_kids(int count){
		kids = count;
	}
	
	/**
	 * Mark current record to be skipped ( not included in xml response )
	 */
	public void skip(){
		skip = true;
	}
	
	/**
	 * Convert data item to xml representation
	 * 
	 * @param out the output buffer
	 */
	public void to_xml(StringBuffer out){
		to_xml_start(out);
		to_xml_end(out);
	}
	
	/**
	 * Starting part of xml representation
	 * 
	 * @param out the output buffer
	 */
	public void to_xml_start(StringBuffer out){
		out.append("<item");
		for (int i=0; i<config.data.size(); i++){
			String value = get_value(config.data.get(i).name);
			if (value==null) value="";
			out.append(" ");
			out.append(config.data.get(i).name);
			out.append("='");
			out.append(xmlentities(value));
			out.append("'");
		}
		out.append(userdata_to_xml());
		out.append(">");
	}
	
	/**
	 * Ending part of xml representation.
	 * 
	 * @param out the output buffer
	 */
	public void to_xml_end(StringBuffer out){
		out.append("</item>");
	}
	
	/**
	 * Convert data item to json representation
	 * 
	 * @param JSON object
	 */
	public void to_json(JSONArray output) {}


	/**
	 * replace xml unsafe characters
	 * 
	 * @param string string to be escaped
	 * @return escaped string 
	 */
	public String xmlentities(String str) {
		str = str.replace("&", "&amp;");
		str = str.replace("\"", "&quot;");
		str = str.replace("'", "&apos;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace("’", "&apos;");
		return str;
	}
	
	public void set_userdata(String name, String value) {
		if (userdata == null)
			userdata = new HashMap<String, String>();
		userdata.put(name, value);
	}
	
	protected String userdata_to_xml() {
		StringBuffer out = new StringBuffer();
		if (userdata!=null)
			for (Object key : userdata.keySet()) {
			    Object value = userdata.get(key);
			    out.append(" " + key.toString() + "='" + xmlentities(value.toString()) + "'");
			}
		return out.toString();
	}
	
	protected String userdata_to_xmltag() {
		StringBuffer out = new StringBuffer();

		if (userdata!=null)
			for (Object key : userdata.keySet()) {
			    String tag =key.toString(); 
			    out.append("<" + tag + ">");
				out.append("<![CDATA[" + userdata.get(key).toString() + "]]>");
				out.append("</" + tag + ">");
			}
		return out.toString();
	}
	
	protected void userdata_to_json(JSONObject record) {
		if (userdata!=null)
			for (Object key : userdata.keySet()) {
			    Object value = userdata.get(key);
			    record.put(key.toString(), xmlentities(value.toString()));
			}
	}
}
