/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * The Class DataRequest.
 * 
 * Contains info about specific rules of current data request
 */
public class DataRequest {
	
	/** The set of filtering rules */
	private ArrayList<FilteringRule> filters;
	
	/** The set of sorting rules */
	private ArrayList<SortingRule> sort_by;
	
	/** The start position of selection */
	private String start="";
	
	/** The count of requested records */
	private String count="";
	
	/** The relation id */
	private String relation="";
	
	/** The source table */
	private String source="";
	
	/** The list of used fields */
	private String fieldset="";
	
	/** The list of used fields */
	private String order="";
	
	/**
	 * Instantiates a new data request.
	 */
	public DataRequest() {
		filters = new ArrayList<FilteringRule>();
		sort_by = new ArrayList<SortingRule>();
	}
	
	/**
	 * Instantiates a new data request ( copy constructor )
	 * 
	 * @param source the source requets object
	 */
	public DataRequest(DataRequest source) {
		this();
		copy(source);
	}
	
	/**
	 * Copy data from existing request object
	 * 
	 * @param original the source request object
	 */
	public void copy(DataRequest original){
		//potential problems with object sharing
		filters = 	original.get_filters();
		sort_by = 	original.get_sort_by();
		count = 	original.get_count();
		start = 	original.get_start();
		source = 	original.get_source();
		fieldset =	original.get_fieldset();
		relation = 	original.get_relation();
	}
	
	/**
	 * Gets the name of source table
	 * 
	 * @return the source table
	 */
	public String get_source() {
		return source;
	}

	/**
	 * Gets the start position
	 * 
	 * @return the index of first element in selection, which was requested by client side code
	 */
	public String get_start() {
		return start;
	}

	/**
	 * Gets the requested count.
	 * 
	 * @return the count of records, which was requested by client side code
	 */
	public String get_count() {
		return count;
	}

	/**
	 * Gets the list of used fields.
	 * 
	 * @return the list of used fields
	 */
	public String get_fieldset() {
		return fieldset;
	}

	/**
	 * Gets the set of filter rules
	 * 
	 * @return the set of filter rules
	 */
	public ArrayList<FilteringRule> get_filters() {
		return filters;
	}

	/**
	 * Gets the relation id
	 * 
	 * @return the name of relation id field
	 */
	public String get_relation() {
		return relation;
	}
	
	public String get_order() {
		return order;
	}

	/**
	 * Gets the set of applied sorting rules
	 * 
	 * @return the set of sorting rules
	 */
	public ArrayList<SortingRule> get_sort_by() {
		return sort_by;
	}

	/**
	 * Sets the list of used fields
	 * 
	 * @param value the list of used fields
	 */
	public void set_fieldset(String value) {
		fieldset = value;
	}
	
	/**
	 * Set_filter.
	 * 
	 * @param field the field by which filtering will be applied
	 * @param value the value
	 * @param rule the sql compatible rule 
	 */
	public void set_filter(String field, String value, String rule){
		filters.add(new FilteringRule(field,value,rule));
	}
	
	/**
	 * Set the filtering rule. ( LIKE )
	 * 
	 * @param field the field by which filtering will be applied
	 * @param value the value
	 */
	public void set_filter(String field,String value){
		set_filter(field,value,"");
	}
	
	/**
	 * Sets the filtering rule
	 * 
	 * @param sql the sql string with the filtering rule
	 */
	public void set_filter(String sql){
		filters.add(new FilteringRule(sql));
	}
	
	/**
	 * Clears filter rules set
	 */
	public void clear_filters() {
		filters.clear();
	}
	
	/**
	 * Sets the sorting order
	 * 
	 * @param column the name of column by which selection will be sorted (asc order)
	 */
	public void set_sort(String column) {
		set_sort(column,"");
	}
	
	/**
	 * Sets sorting order for selection
	 * 
	 * @param column the column by which sorting will be executed
	 * @param direction the direction of sorting
	 */
	public void set_sort(String column, String direction) {
		if (column == null || column.equals(""))
			sort_by = new ArrayList<SortingRule>();
		else
			sort_by.add(new SortingRule(column,direction));
	}
	
	/**
	 * Sets selection limit
	 * 
	 * @param start the start position
	 * @param count the count of records to be selected
	 */
	public void set_limit(String start, String count){
		this.count = count;
		this.start = start;
	}
	
	/**
	 * Sets selection limit
	 * 
	 * @param start the start position
	 * @param count the count of records to be selected
	 */
	public void set_limit(int start, int count) {
		set_limit(Integer.toString(start),Integer.toString(count));
	}
	
	/**
	 * Sets the name of source table
	 * 
	 * @param name the source table name
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void set_source(String name) throws ConnectorConfigException{
		source = name.trim();
		if (source.equals(""))
			throw new ConnectorConfigException("Source of data can't be empty");
	}
	
	/**
	 * Sets the name of relation id field
	 * 
	 * @param relation the new _relation
	 */
	public void set_relation(String relation){
		this.relation = relation;
	}
	
	public void set_order(String order){
		this.order = order;
	}
	
	/**
	 * Parse SQL string, extract table name, list of fields, list of rules
	 * 
	 * @param sql the sql string
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void parse_sql(String sql, Boolean as_is) throws ConnectorConfigException{
		if (as_is){
			fieldset = sql;
			return;
		}
		Pattern limit_regex = Pattern.compile("[ \n]+limit[\n ,0-9]", Pattern.CASE_INSENSITIVE);
		Pattern where_regex = Pattern.compile("[ \n]+where", Pattern.CASE_INSENSITIVE);
		Pattern from_regex = Pattern.compile("[ \n]+from", Pattern.CASE_INSENSITIVE);
		Pattern select_regex = Pattern.compile("select", Pattern.CASE_INSENSITIVE);
		Pattern order_regex = Pattern.compile("[ \n]+order[ ]+by", Pattern.CASE_INSENSITIVE);
		Pattern empty_regex = Pattern.compile("[ ]+", Pattern.CASE_INSENSITIVE);
		Pattern groupby_regex = Pattern.compile("[ \n]+group[ \n]+by[ \n]+", Pattern.CASE_INSENSITIVE);

		sql = limit_regex.split(sql)[0]; //drop limit part;

		if (groupby_regex.split(sql).length > 1){ //workaround for GROUP BY in sql
			set_source("("+sql+") dhx_group_table");
			return;
		}
			
		//locate select part
		String[] data = from_regex.split(sql,2);
		set_fieldset(select_regex.split(data[0],2)[1]);
		
		String [] table_data = where_regex.split(data[1],2);
		if (table_data.length>1){ //where construction exists
			set_source(table_data[0]);
			String [] where_data = order_regex.split(table_data[1]);
			set_filter(where_data[0]);
			if (where_data.length==1) return; //all parsed
			sql = where_data[1].trim();
		} else { //check order 
			String [] order_data = order_regex.split(table_data[0],2);
			set_source(order_data[0]);
			if (order_data.length==1) return; //all parsed
			sql = order_data[1].trim();
		}
		
		if (!sql.equals("")){
			String [] order_details = empty_regex.split(sql);
			String order_direction = order_details.length > 1 ? order_details[1] : "ASC";
            set_sort(order_details[0],order_direction);
		}
	}
	
	public void parse_sql(String sql) throws ConnectorConfigException{
		parse_sql(sql, false);
	}
}
