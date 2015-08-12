/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class DataConfig.
 * 
 * Class used to store configuration of Tables and Fields which are used for data operations
 */
public class DataConfig {

	/** The relation id */
	public ConnectorField relation_id;
	
	/** The id */
	public ConnectorField id;
	
	/** The data fields */
	public ArrayList<ConnectorField> text;
	
	/** The extra data fields */
	public ArrayList<ConnectorField> data;

	/**
	 * Instantiates a new data config.
	 */
	public DataConfig(){
		text = new ArrayList<ConnectorField>();
		data = new ArrayList<ConnectorField>();
		id = new ConnectorField();
		relation_id = new ConnectorField();
	}
	
	/**
	 * Instantiates a new data config ( copy constructor )
	 * 
	 * @param original the original
	 */
	public DataConfig(DataConfig original){
		this();
		copy(original);
	}
	
	/**
	 * Copy config settings
	 * 
	 * @param original the original, from which settings will be copied
	 */
	public void copy(DataConfig original) {
		id = original.id;
		relation_id = original.relation_id;
		text = original.text;
		data = original.data;
	}
	
	/**
	 * Minimize configuration, by removing unused fields.
	 * 
	 * @param name the field which need to be preserved
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void minimize(String name) throws ConnectorConfigException{
		for (int i=0; i<text.size(); i++){
			ConnectorField field = text.get(i);
			if (field.name.equals(name)){
				this.text = new ArrayList<ConnectorField>();
				this.text.add(new ConnectorField(name,"value"));
				this.data = new ArrayList<ConnectorField>();
				this.data.add(new ConnectorField(name,"value"));
				return;	
			}
		}
		throw new ConnectorConfigException("Incorrect dataset minimization, master field not found.");
	}
	
	/**
	 * Initialize  the configuration
	 * 
	 * @param id the id
	 * @param fields the data fields
	 * @param extra the extra data fields
	 * @param relation the relation id 
	 */
	public void init(String id, String fields, String extra, String relation){
		this.id = parse_one(id);
		this.relation_id = parse_one(relation);
		
		parse_many(fields,this.text);
		parse_many(fields,this.data);
		parse_many(extra, this.data);
	}
	
	/**
	 * Parse string description of multiple fields
	 * 
	 * @param key the string to be parsed
	 * @param collection the target collection
	 */
	private void parse_many(String key, ArrayList<ConnectorField> collection){
		String[] keys = key.trim().split(",");
		for (int i=0; i<keys.length; i++)
			if (!keys[i].trim().equals(""))
				collection.add(parse_one(keys[i]));
	}
	
	/**
	 * Parse string description of single field
	 * 
	 * @param key the string to be parsed
	 * 
	 * @return the connector field
	 */
	private ConnectorField parse_one(String key){
		key=key.trim();
		int sub = key.indexOf("(");
		if (sub==-1)
			return new ConnectorField(key);
		else
			return new ConnectorField(key.substring(0,sub),key.substring(sub+1,key.length()-1));
	}
	
	/**
	 * Returns the list of all involved db fieds 
	 * 
	 * @return the list of all involved db fieds
	 */
	public String db_names_list(){
		StringBuffer list = new StringBuffer();
		if (!id.isEmpty())
			list.append(id.db_name);
		if (!relation_id.isEmpty()){
			if (list.length()!=0)
				list.append(",");
			list.append(relation_id.db_name);
		}
		
		for (int i=0; i<data.size(); i++){
			ConnectorField field = data.get(i);
			if (list.length()!=0)
				list.append(",");
			
			list.append(field.db_name);
			if (!field.db_name.equals(field.name)) 
				list.append(" as "+field.name);
		}
		
		return list.toString();
	}
	
	/**
	 * add new field to the configuration
	 * 
	 * @param name the name of field
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void add_field(String name) throws ConnectorConfigException{
		add_field(name,name);
	}
	
	/**
	 * add new field to the configuration
	 * 
	 * @param name the name of the field
	 * @param alias the alias of the field
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void add_field(String name, String alias) throws ConnectorConfigException{
		if (id.db_name.equals(name) || relation_id.db_name.equals(name)){
			//TODO log 
		}
		if (is_field(name,text)!=-1)
			throw new ConnectorConfigException("There was no such data field registered as: "+name);
		
		text.add(new ConnectorField(name,alias));
		if (is_field(name,data)==-1)
			data.add(new ConnectorField(name,alias));
	}
	
	/**
	 * remove field from configuration
	 * 
	 * @param name the name of field
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void remove_field(String name) throws ConnectorConfigException{
		int index = is_field(name);
		if (index == -1)
			throw new ConnectorConfigException("There was no such data field registered as: "+name);
		text.remove(index);
	}
	
	/**
	 * remove field from configuration at all
	 * 
	 * @param name the name of field
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	public void remove_field_full(String name) throws ConnectorConfigException{
		int index = is_field(name);
		if (index == -1)
			throw new ConnectorConfigException("There was no such data field registered as: "+name);
		text.remove(index);

		index = is_field(name, data);
		if (index == -1)
			throw new ConnectorConfigException("There was no such data field registered as: "+name);
		data.remove(index);
	}
	
	/**
	 * Checks is field with such name already registered in collection of data fields
	 * 
	 * @param name the name of field
	 * 
	 * @return the index of field in collection, -1 if field with such name wasn't found
	 */
	public int is_field(String name){
		return is_field(name,text);
	}
	
	/**
	 * Checks is field with such name already registered
	 * 
	 * @param name the name of field
	 * @param collection the collection against which check will be executed
	 * 
	 * @return the index of field in collection, -1 if field with such name wasn't found
	 */
	private int is_field(String name,ArrayList<ConnectorField> collection){
		for (int i=0; i<collection.size(); i++){
			ConnectorField field = collection.get(i);
			if (field.name.equals(name) || field.db_name.equals(name))
				return i;
		}
			
		return -1;
	}
}
