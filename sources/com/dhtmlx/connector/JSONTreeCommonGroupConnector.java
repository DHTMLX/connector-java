package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.simple.JSONObject;

public class JSONTreeCommonGroupConnector extends JSONCommonConnector {

	protected JSONCommonGroupRenderStrategy render;

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 */
	public JSONTreeCommonGroupConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public JSONTreeCommonGroupConnector(Connection db, DBType db_type){
		this(db,db_type, new JSONCommonGroupFactory());
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public JSONTreeCommonGroupConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,(JSONCommonGroupRenderStrategy) a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new tree connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the class strategy, which will render items
	 */
	public JSONTreeCommonGroupConnector(Connection db, DBType db_type, BaseFactory a_factory, JSONCommonGroupRenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
		render = render_type;
		render.init(this);
		event.attach(new TreeGridBehavior(config));
	}

	/** if not isset $_GET[id] then it's top level
	 */
	protected void set_relation() {
		if (http_request.getParameter(parent_name)==null) request.set_relation("");
	}

	/** if it's first level then distinct level
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

	protected String render_set(ConnectorResultSet result) throws ConnectorOperationException {
		JSONObject data = new JSONObject();
		data.put("parent", request.get_relation());
		ArrayList<JSONObject> dat = render.render_json(result, cfactory, dynloading, encoding, config);
		data.put("data", dat);
		for (Object key : attributes.keySet()) {
		    data.put(key.toString(), attributes.get(key).toString());
		}
		return data.toString();
	}
	
	protected String xml_start() { return "";}
	protected String xml_end() { return "";}
}
