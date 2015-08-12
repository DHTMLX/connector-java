/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;

/**
 * The Class ComboConnector.
 * 
 * Represents server side backend for dhtmlxcombo
 */
public class ComboConnector extends BaseConnector {

	/**
	 * Instantiates a new combo connector.
	 * 
	 * @param db the db connection
	 */
	public ComboConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new combo connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public ComboConnector(Connection db, DBType db_type){
		this(db,db_type, new ComboFactory());
	}
	
	/**
	 * Instantiates a new combo connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object 
	 */
	public ComboConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#parse_request()
	 */
	@Override
	protected void parse_request() {
		super.parse_request();
		
		String pos = http_request.getParameter("pos");
		if (pos!=null && dynloading)
			request.set_limit(pos,Integer.toString(dynloading_size));
		
		String mask = http_request.getParameter("mask");
		if (mask!=null)
			request.set_filter(config.text.get(0).name, mask+"%", "LIKE");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#xml_start()
	 */
	@Override
	protected String xml_start() {
		String pos = this.request.get_start();
		if (pos!=null && !pos.equals("") && !pos.equals("0"))
			add_top_attribute("add", "true");
		return "<complete" + top_attributes() + ">";
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#xml_end()
	 */
	@Override
	protected String xml_end() {
		return "</complete>";
	}
	
}
