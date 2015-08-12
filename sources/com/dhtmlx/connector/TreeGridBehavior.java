/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;
import java.util.HashMap;

/**
 * The Class TreeGridBehavior.
 * 
 * Desribes the auto-update rules, which will be used for hierarchical structures
 */
public class TreeGridBehavior extends ConnectorBehavior {
	
	/** The id swap table */
	private HashMap<String,String> id_swap = new HashMap<String,String>();
	
	/** The config */
	private DataConfig config;
	
	/**
	 * Instantiates a new tree grid behavior.
	 * 
	 * @param config the config
	 */
	public TreeGridBehavior(DataConfig config){
		this.config=config;
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorBehavior#afterInsert(com.dhtmlx.connector.DataAction)
	 */
	@Override
	public void afterInsert(DataAction action) {
		id_swap.put(action.get_id(),action.get_new_id());
		super.afterInsert(action);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.ConnectorBehavior#beforeProcessing(com.dhtmlx.connector.DataAction)
	 */
	@Override
	public void beforeProcessing(DataAction action) {
		String key = id_swap.get(action.get_value(config.relation_id.name));
		if (key!=null)
			action.set_value(config.relation_id.name,key);
		super.beforeProcessing(action);
	}

}
