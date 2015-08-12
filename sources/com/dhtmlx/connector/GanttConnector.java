/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class SchedulerConnector.
 */
public class GanttConnector extends BaseConnector {
	/**
	 * Instantiates a new gantt connector.
	 * 
	 * @param db the db connection
	 */
	public GanttConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new gantt connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public GanttConnector(Connection db, DBType db_type){
		this(db,db_type, new GanttFactory());
	}
	
	/**
	 * Instantiates a new gantt connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public GanttConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}

	/**
	 * Instantiates a new gantt connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the render class, which will be used to render items
	 */
	public GanttConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
		event.attach(new GanttDefaultBehavior(this));
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#parse_request()
	 */
	@Override
	protected void parse_request() {
		super.parse_request();
		
		if (config.text.size() > 0) {
			String to = http_request.getParameter("to");
			String from = http_request.getParameter("from");
			if (to!=null && !to.equals(""))
				request.set_filter(config.text.get(0).name,to,"<");
			if (from!=null && !from.equals(""))
				request.set_filter(config.text.get(1).name,from,">");
		}
	}


	/**
	 * Define fixed list of options
	 * 
	 * @param name the name of column
	 * @param object the iterable object ( array ) 
	 */
	@SuppressWarnings("unchecked")
	public void set_options(String name, Iterable object){
		Iterator it = object.iterator();
		StringBuffer data = new StringBuffer();
		
		while (it.hasNext()){
			String value = it.next().toString();
			data.append("<item value='"+value+"' label='"+value+"' />");
		}
		set_options(name,new DummyStringConnector(data.toString()));
	}
	
	/**
	 * Define fixed list of options
	 * 
	 * @param name the name of column
	 * @param object the hash object
	 */
	@SuppressWarnings("unchecked")
	public void set_options(String name, HashMap object){
		Iterator it = object.keySet().iterator();
		StringBuffer data = new StringBuffer();
		
		while (it.hasNext()){
			Object value = it.next();
			Object label = object.get(value).toString();
			data.append("<item value='"+value.toString()+"' label='"+label.toString()+"' />");
		}
		set_options(name,new DummyStringConnector(data.toString()));
	}	
	
    public void render_links(String table, String id, String fields) {
    	OptionsConnector links = new OptionsConnector(get_connection(), db_type);
        links.render_table(table, id, id+","+fields);
        set_options("links", links);
    }
    
    @Override
    public String render() {
    	String mode = http_request.getParameter("gantt_mode");
		if (mode != null && mode.equals("links")){
			OptionsConnector links = (OptionsConnector)options.get("links");
			if (links != null){
				links.servlet(http_request, http_response);
				links.render_save();
				return null;
			}
    	}

    	return super.render();
    }
}
