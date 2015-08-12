package com.dhtmlx.connector;

import java.sql.Connection;

public class JSONOptionsConnector extends JSONCommonConnector {

	/** The init flag. */
	protected boolean init_flag = false;
	
	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 */
	public JSONOptionsConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public JSONOptionsConnector(Connection db, DBType db_type){
		this(db,db_type, new JSONCommonFactory());
	}

	/**
	 * Instantiates a new options connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory
	 */
	public JSONOptionsConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory,new JSONRenderStrategy());
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render()
	 */
	@Override
	public String render(){
		if (!init_flag){
			init_flag = true;
			return "";
		}

		ConnectorResultSet res = get_resource();
		try {
			return super.render_set(res);
		} catch (ConnectorOperationException e) {
			return "";
		}
	}
	
	public void render_save(){
		super.render();
	}
}
