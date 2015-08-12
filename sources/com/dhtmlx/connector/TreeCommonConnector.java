package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;

public class TreeCommonConnector extends CommonConnector {

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 */
	public TreeCommonConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public TreeCommonConnector(Connection db, DBType db_type){
		this(db,db_type, new TreeCommonFactory());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public TreeCommonConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param a_factory the class, which will render items
	 */
	public TreeCommonConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
	}
	
	public String xml_start() {
		add_top_attribute("parent", request.get_relation());
		return "<data" + top_attributes() + ">";
	}

	@Override
	protected void parse_request() {
		super.parse_request();
		
		String id = http_request.getParameter("id");
		if (id!=null)
			request.set_relation(id);
		else
			request.set_relation("0");
	}

}
