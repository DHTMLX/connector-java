/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class DataProcessor.
 * 
 * Handles "update" part of logic.
 * This class can be used as a base for component specific data processors. 
 *  
 */
public class DataProcessor {
	
	/** The master connector. */
	protected BaseConnector connector;
	
	/** The config. */
	protected DataConfig config;
	
	/** The request. */
	protected DataRequest request;
	
	/** The class factory. */
	protected BaseFactory cfactory;
	
	public static String action_param = "!nativeeditor_status";
	
	/**
	 * Instantiates a new data processor.
	 * 
	 * @param connector the connector
	 * @param config the config
	 * @param request the request
	 * @param cfactory the class factory
	 */
	public DataProcessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		this.connector = connector;
		this.config = config;
		this.request = request;
		this.cfactory = cfactory;
	}
	
	/**
	 * Convert incoming parameter name to name of DB field
	 * 
	 * @param name the parameter name
	 * 
	 * @return the DB field name
	 */
	protected String name_data(String name){
		return name;
	}
	
	/**
	 * Sort incoming data, creates a hash of data for each record in incoming request 
	 * 
	 * @param ids the array of record ids, data for which need to be allocated
	 * 
	 * @return the id to record hash
	 */
	protected HashMap<String, HashMap<String,String>> get_post_values(String [] ids){
		HashMap<String, HashMap<String,String>> data = new HashMap<String, HashMap<String,String>>();
		for (int i=0; i<ids.length; i++){
			data.put(ids[i],new HashMap<String, String>());
			if (!config.id.name.equals(""))
				data.get(ids[i]).put(config.id.name,ids[i]);
		}
		 
		Iterator<String> it = connector.incoming_data.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			if (key.indexOf("_")==-1) continue;
			
			for (int i=0; i<ids.length; i++)
				if (key.indexOf(ids[i]) == 0){
					String field = key.replace(ids[i]+"_","");
					String realname = name_data(field);
					String value = connector.incoming_data.get(key);
					value = new ConnectorSecurity().filter(value);
					if (!realname.equals(""))
						data.get(ids[i]).put(realname,value);
					break;
				}
		}
		
		return data;
	}
	
	/**
	 * Process incoming request
	 * 
	 * Detects the list of the operation in incoming request
	 * Process operations one by one
	 * 
	 * @return the xml string, representing result of operation
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 * @throws ConnectorConfigException the connector config exception
	 */
	public String process() throws ConnectorOperationException, ConnectorConfigException{
		ArrayList<DataAction> result = new ArrayList<DataAction>();
		
		String ids = get_list_of_ids();
		if (ids==null)
			throw new ConnectorOperationException("Incorrect incoming data, ID of incoming records not recognized");
		
		String [] id_keys = ids.split(",");
		HashMap<String, HashMap<String,String>> data = get_post_values(id_keys);
		boolean failed = false;
		
		try {
			if (connector.sql.is_global_transaction())
				connector.sql.begin_transaction();
			
			for (int i=0; i<id_keys.length; i++){
				String id = id_keys[i];
				HashMap<String,String> item_data = data.get(id);
				String status = get_status(item_data);
				
				DataAction action = get_data_action(status,id,item_data);
				result.add(action);
				inner_process(action);
			}
		} catch (ConnectorOperationException e) {
			connector.event.trigger().afterDBError(result.get(result.size()-1), e);
			failed = true;
		}

		if (connector.sql.is_global_transaction()){
			if (!failed)
				for (int i=0; i<result.size(); i++){
					String result_status = result.get(i).get_status();
					if (result_status.equals("error") || result_status.equals("invalid")){
						failed = true;
						break;
					}
				}
			if (failed){
				for (int i=0; i<result.size(); i++)
					result.get(i).error();
				connector.sql.rollback_transaction();
			} else {
				connector.sql.commit_transaction();
			}
		}
		
		return output_as_xml(result);
	}
	
	protected DataAction get_data_action(String status, String id,
			HashMap<String, String> itemData) {
		return new DataAction(status, id, itemData);
	}

	protected String get_status(HashMap<String, String> itemData) {
		return itemData.get(DataProcessor.action_param);
	}

	protected String get_list_of_ids(){
		return connector.incoming_data.get("ids");
	}

	/**
	 * Convert incoming client side status, to DB operation
	 * 
	 * @param status the status from incoming request
	 * 
	 * @return the operation type
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	protected OperationType status_to_mode(String status) throws ConnectorOperationException{
		if (status.equals("updated") || status.equals("update")) return OperationType.Update;
		else if (status.equals("inserted") || status.equals("insert")) return OperationType.Insert;
		else if (status.equals("deleted") || status.equals("delete")) return OperationType.Delete;
		
		throw new ConnectorOperationException("Unknown action type: "+status);
	}
	
	
	/**
	 * Convert state to xml string
	 * 
	 * @param result the list of data actions , created during processing 
	 * 
	 * @return the xml string
	 */
	protected String output_as_xml(ArrayList<DataAction> result) {
		StringBuffer out = new StringBuffer();
		out.append("<data>");
		for (int i=0; i<result.size(); i++)
			out.append(result.get(i).to_xml());
		out.append("</data>");
		
		return out.toString(); 
	}

	/**
	 * Inner processing routine, called for each record in incoming request
	 * 
	 * @param action the data action which need to be processed
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 * @throws ConnectorOperationException the connector operation exception
	 */
	private void inner_process(DataAction action) throws ConnectorConfigException, ConnectorOperationException {
		if (connector.sql.is_record_transaction())
			connector.sql.begin_transaction();
		
		try {
			OperationType mode  = status_to_mode(action.get_status());
			if (!connector.access.check(mode))
				action.error();
			else {
				connector.event.trigger().beforeProcessing(action);
				if (!action.is_ready())
					check_exts(action,mode);
				
				//correcting order of new record
				if (mode.equals("insert") && !action.get_status().equals("error") && !action.get_status().equals("invalid"))
					((DBDataWrapper)connector.sql).new_order(action,  request);
				
				connector.event.trigger().afterProcessing(action);
			}
		} catch (ConnectorConfigException e) {
			action.set_status("error");
			throw e;
		} catch (ConnectorOperationException e) {
			action.set_status("error");
			throw e;
		}
		
		if (connector.sql.is_record_transaction()){
			if (action.get_status().equals("invalid") || action.get_status().equals("error"))
				connector.sql.rollback_transaction();
			else
				connector.sql.commit_transaction();
		}
	}

	/**
	 * Checks if there an external event or SQL code was defined for current action
	 * 
	 * @param action the action
	 * @param mode the operation type
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 * @throws ConnectorOperationException the connector operation exception
	 */
	private void check_exts(DataAction action, OperationType mode) throws ConnectorConfigException, ConnectorOperationException {
		switch(mode){
			case Delete:
				connector.event.trigger().beforeDelete(action);
				break;
			case Insert:
				connector.event.trigger().beforeInsert(action);
				break;
			case Update:
				connector.event.trigger().beforeUpdate(action);
				break;
			case Order:
				connector.event.trigger().beforeOrder(action);
				break;
		}
		
		if (!action.is_ready()){
			String sql = connector.sql.get_sql(mode,action.get_data());
			if (sql!=null && !sql.equals("")){
				((DBDataWrapper)connector.sql).query(sql);
				if (mode.equals(OperationType.Insert))
					action.success(((DBDataWrapper)connector.sql).get_new_id(null));
			} else {
				action.sync_config(config);
				switch(mode){
					case Delete:
						connector.sql.delete(action, request);
						break;
					case Insert:
						connector.sql.insert(action, request);
						break;
					case Update:
						connector.sql.update(action, request);
						break;
					case Order:
						connector.sql.update(action, request);
						break;
				}
			}
		}
		
		switch(mode){
			case Delete:
				connector.event.trigger().afterDelete(action);
				break;
			case Insert:
				connector.event.trigger().afterInsert(action);
				break;
			case Update:
				connector.event.trigger().afterUpdate(action);
				break;
			case Order:
				connector.event.trigger().afterOrder(action);
				break;
		}
		
	}
}
