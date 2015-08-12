package com.dhtmlx.connector;

import java.sql.Connection;

public class TreeGroupConnector extends TreeConnector {

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 */
	public TreeGroupConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public TreeGroupConnector(Connection db, DBType db_type){
		this(db,db_type, new TreeGroupFactory());
	}
	
	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public TreeGroupConnector(Connection db, DBType db_type, BaseFactory a_factory){
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
	public TreeGroupConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
		event.attach(new TreeGridBehavior(config));
	}

	/*! if not isset $_GET[id] then it's top level
	 */
	protected void set_relation() {
		if (http_request.getParameter(parent_name)==null) request.set_relation("");
	}

	/*! if it's first level then distinct level
	 *  else select by parent
	 */
	protected ConnectorResultSet get_resource() {
		ConnectorResultSet resource = null;
		try {
			if (http_request.getParameter(parent_name)!=null)
				resource = sql.select(request);
			else
				resource = sql.get_variants(request, config.relation_id.db_name);
		} catch (ConnectorOperationException e) {
			e.printStackTrace();
		}
		return resource;
	}

	/*! renders self as xml, starting part
	*/
	public String xml_start(){
		String parent = http_request.getParameter(parent_name);
		if (parent!=null)
			add_top_attribute("id", parent + render.get_postfix());
		else
			add_top_attribute("id", "0");
		return "<tree" + top_attributes() + ">";
	}
}
