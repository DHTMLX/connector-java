package com.dhtmlx.connector;

import java.sql.Connection;
import org.json.simple.JSONObject;

public class JSONTreeCommonConnector extends TreeCommonConnector {

	protected JSONTreeRenderStrategy render;
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 */
	public JSONTreeCommonConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public JSONTreeCommonConnector(Connection db, DBType db_type){
		this(db,db_type, new JSONTreeCommonFactory());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public JSONTreeCommonConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory, (JSONTreeRenderStrategy) a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the class, which will renderitems
	 */
	public JSONTreeCommonConnector(Connection db, DBType db_type, BaseFactory a_factory, JSONTreeRenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
		render = render_type;
		render.init(this);
	}

	protected String render_set(ConnectorResultSet result) throws ConnectorOperationException {
		JSONObject data = new JSONObject();
		data.put("parent", request.get_relation());
		data.put("data", render.render_json(result, dynloading, config, mix));
		return data.toString();
	}

	protected String output_as_xml(ConnectorResultSet result) throws ConnectorOperationException{
		String data = render_set(result);
		if (as_string)
			return data;

		ConnectorOutputWriter out = new ConnectorOutputWriter(data, "");
		out.set_type(ExportServiceType.JSON);
		event.trigger().beforeOutput(out, http_request, http_response);
		out.output(http_response, encoding);
		return data.toString();
	}

}
