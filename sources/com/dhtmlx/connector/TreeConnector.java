/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;

/**
 * The Class TreeConnector.
 */
public class TreeConnector extends BaseConnector {

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 */
	public TreeConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public TreeConnector(Connection db, DBType db_type){
		this(db,db_type, new TreeFactory());
	}
	
	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public TreeConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the class strategy, which will render items
	 */
	public TreeConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
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
	 * @see com.dhtmlx.connector.BaseConnector#xml_start()
	 */
	@Override
	protected String xml_start() {
		add_top_attribute("id", request.get_relation());
		return "<tree" + top_attributes() + ">";
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#xml_end()
	 */
	@Override
	protected String xml_end() {
		return "</tree>";
	}
	
	
	
}
