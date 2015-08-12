/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;

/**
 * The Class TreeGridConnector.
 */
public class TreeGridConnector extends GridConnector {
	
	/**
	 * Instantiates a new tree grid connector.
	 * 
	 * @param db the db connection
	 */
	public TreeGridConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new tree grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public TreeGridConnector(Connection db, DBType db_type){
		this(db,db_type, new TreeGridFactory());
	}
	
	/**
	 * Instantiates a new tree grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public TreeGridConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new tree grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the class, which will render items
	 */
	public TreeGridConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
		event.attach(new TreeGridBehavior(config));
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#set_relation()
	 */
	@Override
	protected void set_relation() {
		String id = http_request.getParameter("id");
		if (id!=null)
			request.set_relation(id);
		else
			request.set_relation("0");
		
		request.set_limit("","");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.GridConnector#xml_start()
	 */
	@Override
	protected String xml_start() {
		add_top_attribute("parent", request.get_relation());
		return "<rows" + top_attributes() + ">";
	}

}
