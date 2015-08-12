package com.dhtmlx.connector;

import java.sql.Connection;

public class MultitableTreeConnector extends TreeConnector {
	
	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 */
	public MultitableTreeConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public MultitableTreeConnector(Connection db, DBType db_type){
		this(db,db_type, new MultitableTreeFactory());
	}

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public MultitableTreeConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the class, which renders items
	 */
	public MultitableTreeConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
	}

	public String render() {
		dynloading = true;
		return super.render();
	}

	/**
	 * Render DB result set as XML string
	 * 
	 * @param result the DB result
	 * 
	 * @return the xml string
	 * 
	 * @throws ConnectorOperationException the connector operation exception 
	 */
	protected String render_set(ConnectorResultSet result) throws ConnectorOperationException{
		return render.render_set(result, cfactory, dynloading, "\n", config, mix);
	}

	protected void set_relation() {
		super.set_relation();
		String parent = http_request.getParameter(parent_name);
		if (parent==null)
			request.set_relation("");
		else
			request.set_relation(render.parse_id(parent));
	}

	protected String xml_start() {
		String parent = http_request.getParameter(parent_name);
		if (parent!=null) {
			add_top_attribute("id", parent);
		} else {
			add_top_attribute("id", "0");
		}
		return "<tree" + top_attributes() + ">";
	}

	public Integer get_level() {
		return render.get_level();
	}

	public void set_max_level(Integer max) {
		render.set_max_level(max);
	}

}
