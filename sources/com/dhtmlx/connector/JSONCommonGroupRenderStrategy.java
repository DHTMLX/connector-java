package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class JSONCommonGroupRenderStrategy extends GroupRenderStrategy {

	/**
	 * Render DB result set as XML string
	 * @param result - the DB result
	 * @param cfactory - the connector factory object
	 * @param dload - dynloading flag
	 * @param sep - items separator
	 * @return the xml string
	 * @throws ConnectorOperationException the connector operation exception 
	 */
	protected ArrayList<JSONObject> render_json(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config)
	throws ConnectorOperationException{
		ArrayList<JSONObject> output = new ArrayList<JSONObject>();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			Boolean has_kids;
			if (values.get(config.id.name)!=null) {
				has_kids = false;
			} else {
				values.put(config.id.name, values.get("value") + id_postfix);
				values.put(config.text.get(0).name, values.get("value"));
				has_kids = true;
			}
			JSONTreeCommonDataItem data = (JSONTreeCommonDataItem) cfactory.createDataItem(values, config, index);
			if (data.get_id()==null)
				data.set_id(conn.uuid());
			conn.event.trigger().beforeRender(data);
			if (has_kids==false)
				data.set_kids(0);
			
			if (data.has_kids()==0 && dload)
				data.set_kids(1);

			JSONObject record = data.to_xml_start();

			if (data.has_kids()!=0 || (data.has_kids()==1 && !dload && has_kids==true)) {
				DataRequest sub_request = new DataRequest(conn.request);
				sub_request.set_relation(values.get("value"));
//				ArrayList<JSONObject> temp = render_json(conn.sql.select(sub_request), cfactory, dload, sep, config);
//				if (temp.size() > 0) record.put("data", temp);
			}
			output.add(record);
			index++;
		}
		return output;
	}

}
