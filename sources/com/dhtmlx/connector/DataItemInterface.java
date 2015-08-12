package com.dhtmlx.connector;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface DataItemInterface {

	/**
	 * Gets the value of the field
	 * 
	 * @param name the name of the field
	 * 
	 * @return the value of the field
	 */
	public String get_value(String name);
	/**
	 * Sets value of the field
	 * 
	 * @param name the name of the field
	 * @param value the value of the field
	 */
	public void set_value(String name, String value);

	/**
	 * Gets the id of the record
	 * 
	 * @return the id of the record
	 */
	public String get_id();
	/**
	 * Sets the new id for the record
	 * 
	 * @param value the new id for the record
	 */
	public void set_id(String value);
	/**
	 * Gets the index of the record
	 * 
	 * @return the index of the record
	 */
	public int get_index();

	/**
	 * Gets the count of child items
	 * 
	 * @return the count of child items
	 */
	public int has_kids();

	/**
	 * Sets the count of child items
	 * 
	 * @param count the count of child items
	 */
	public void set_kids(int count);

	/**
	 * Mark current record to be skipped ( not included in xml response )
	 */
	public void skip();

	/**
	 * Convert data item to xml representation
	 * 
	 * @param out the output buffer
	 */
	public void to_xml(StringBuffer out);
	
	/**
	 * Starting part of xml representation
	 * 
	 * @param out the output buffer
	 */
	public void to_xml_start(StringBuffer out);

	/**
	 * Ending part of xml representation.
	 * 
	 * @param out the output buffer
	 */
	public void to_xml_end(StringBuffer out);

	/**
	 * Convert data item to json representation
	 * 
	 * @param JSON object
	 */
	public void to_json(JSONArray output);


	/**
	 * replace xml unsafe characters
	 * 
	 * @param string string to be escaped
	 * @return escaped string 
	 */
	public String xmlentities(String str);
	public void set_userdata(String name, String value);

	
}
