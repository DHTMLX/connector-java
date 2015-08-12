/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class GridConnector.
 */
public class GridConnector extends BaseConnector {
	
	/** The extra info , which need to be mixed in output */
	protected StringBuffer extra_output = new StringBuffer();
	
	/** The collections of options */
	protected HashMap<String,BaseConnector> options = new HashMap<String,BaseConnector>();

	private GridConfiguration gridConfig; 
	
	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 */
	public GridConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public GridConnector(Connection db, DBType db_type){
		this(db,db_type, new GridFactory());
	}
	
	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public GridConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new grid connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public GridConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#parse_request()
	 */
	@Override
	protected void parse_request(){
		super.parse_request();
		
		String colls = http_request.getParameter("dhx_colls");
		if (colls == null && gridConfig != null)
			colls = gridConfig.defineOptions(this);
		
		if (colls!=null){
			try {
				fill_collections(colls.split(","));
			} catch (ConnectorConfigException e) {
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * Fill collections of options with data from DB
	 * 
	 * @param columns the columns for which options are requested
	 * 
	 * @throws ConnectorConfigException the connector config exception
	 */
	protected void fill_collections(String [] columns) throws ConnectorConfigException{
		for (int i=0; i<columns.length; i++){
			String name = resolve_parameter(columns[i]);
			String data = "";
			BaseConnector option_connector = options.get(name);
			if (option_connector==null){
				option_connector = new DistinctOptionsConnector(get_connection(),db_type);
				DataConfig c = new DataConfig(config);
				DataRequest r = new DataRequest(request);
				c.minimize(name);
				option_connector.render_connector(c,r);
			}
			
			data = option_connector.render().toString();
				
			extra_output.append("<coll_options for='"+columns[i]+"'>");
			extra_output.append(data);
			extra_output.append("</coll_options>");
		}
	}
	
	/**
	 * Define connector for options retrieving 
	 * 
	 * @param name the name of column
	 * @param connector the connector
	 */
	public void set_options(String name, BaseConnector connector){
		options.put(name,connector);
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


	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#resolve_parameter(java.lang.String)
	 */
	@Override
	protected String resolve_parameter(String name) {
		try {
			int index = Integer.parseInt(name);
			return config.text.get(index).db_name;
		} catch (NumberFormatException e) {
			return super.resolve_parameter(name); 
		}
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#xml_end()
	 */
	@Override
	protected String xml_end() {
		return extra_output.toString()+"</rows>";
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#xml_start()
	 */
	@Override
	protected String xml_start(){
		String response;
		if (dynloading){
			String pos = request.get_start();
			try{
				if (pos != null && !pos.equals("") && !pos.equals("0"))
					add_top_attribute("pos", pos);
				else
					add_top_attribute("total_count", sql.get_size(request));
			} catch(ConnectorOperationException e){}
			response = "<rows" + top_attributes() + ">";
		}
		else
			response = "<rows>";
		if (this.isFirstLoading() && gridConfig!=null)
			return response + gridConfig.toXML();
		else
			return response;
	}
	
	
	private boolean isFirstLoading() {
		if (http_request.getParameter("dhx_no_header")!=null)
			return false;
		if (http_request.getParameter("posStart")!=null)
			return false;
		if (http_request.getParameter("editing")!=null)
			return false;
		return true;
	}

	public void setConfiguration(GridConfiguration config){
		gridConfig = config; 
	}
}
