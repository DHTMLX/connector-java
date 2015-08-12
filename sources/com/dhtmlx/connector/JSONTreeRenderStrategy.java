package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class JSONTreeRenderStrategy extends TreeRenderStrategy {

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render_set(com.dhtmlx.connector.ConnectorResultSet)
	 */
	protected ArrayList<JSONObject> render_json(ConnectorResultSet result, Boolean dload, DataConfig config, ArrayList<MixinRule> mix)
			throws ConnectorOperationException {
		ArrayList<JSONObject> output = new ArrayList<JSONObject>();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			HashMap<String,Object> item = convert_to_json(values);
			try {
				item = complex_mix(mix, item);
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONTreeCommonDataItem data = (JSONTreeCommonDataItem) conn.cfactory.createDataItem(values, conn.config, index);
			data.set_data(item);
			if (data.get_id()==null) data.set_id(conn.uuid());

			conn.event.trigger().beforeRender(data);

			if (data.has_kids()==-1 && dload)
				data.set_kids(1);

			mix(config, mix);
			JSONObject record = data.to_xml_start();
			unmix(config, mix);

			if (data.has_kids()==-1 || ( data.has_kids()!=0 && !dload)){
				DataRequest sub_request = new DataRequest(conn.request);
				sub_request.set_relation(data.get_id());
				ArrayList<JSONObject> temp = render_json(conn.sql.select(sub_request), dload, config, mix);
				if (temp.size() > 0) record.put("data", temp);
			}
			output.add(record);

			data.to_xml_end(new StringBuffer());
			index++;
		}
		return output;
	}

}
