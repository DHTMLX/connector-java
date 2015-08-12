package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class JSONTreeCommonMultitableConnector extends
		TreeCommonMultitableConnector {
	
	/**
	 * Instantiates a new JSONTreeCommonMultitable connector.
	 * 
	 * @param db the db connection
	 */
	public JSONTreeCommonMultitableConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new JSONTreeCommonMultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public JSONTreeCommonMultitableConnector(Connection db, DBType db_type){
		this(db,db_type, new JSONTreeCommonFactory());
	}

	/**
	 * Instantiates a new JSONTreeCommonMultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the connecto's factory
	 */
	public JSONTreeCommonMultitableConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
	}

	protected String render_set(ConnectorResultSet result) throws ConnectorOperationException {
		JSONObject data = new JSONObject();

		String parent = http_request.getParameter("parent");
		data.put("parent", (parent!=null) ? parent : '0');
		data.put("data", render_set(result, true));
		return data.toString();
		
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render_set(com.dhtmlx.connector.ConnectorResultSet)
	 */
	protected ArrayList<JSONObject> render_set(ConnectorResultSet result, Boolean is_json)
			throws ConnectorOperationException {
		ArrayList<JSONObject> output = new ArrayList<JSONObject>();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			if (values.get(config.id.name)==null) values.put(config.id.name, uuid());
			values.put(config.id.name, level_id(values.get(config.id.name)));
			JSONTreeCommonDataItem data = (JSONTreeCommonDataItem)cfactory.createDataItem(values, config, index);

			event.trigger().beforeRender(data);
			
			if (data.has_kids()==-1 && dynloading)
				data.set_kids(1);

			if (is_max_level()) {
				data.set_kids(-1);
			} else {
				if (data.has_kids()==-1)
					data.set_kids(1);
			}

			JSONObject record = data.to_xml_start();
			output.add(record);
			data.to_xml_end(new StringBuffer());
			index++;
		}
		return output;
	}

	protected String output_as_xml(ConnectorResultSet result) throws ConnectorOperationException{
		String data = render_set(result);

		ConnectorOutputWriter out = new ConnectorOutputWriter(data, "");
		out.set_type(ExportServiceType.JSON);
		event.trigger().beforeOutput(out, http_request, http_response);
		out.output(http_response, encoding);
		return data.toString();
	}
}
