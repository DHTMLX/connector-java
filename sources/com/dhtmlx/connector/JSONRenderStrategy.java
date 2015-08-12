package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;

public class JSONRenderStrategy extends RenderStrategy {

	protected JSONArray render_json(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config, ArrayList<MixinRule> mix)
			throws ConnectorOperationException {
		JSONArray output = new JSONArray();
		int index = 0;
		HashMap<String,String> values;
		mix(config, mix);
		while ( (values = result.get_next()) != null){
			HashMap<String,Object> item = convert_to_json(values);
			try {
				item = complex_mix(mix, item);
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONDataItem data = (JSONDataItem) cfactory.createDataItem(values, conn.config, index);
			data.set_data(item);

			if (data.get_id()==null)
				data.set_id(conn.uuid());

			conn.event.trigger().beforeRender((DataItem) data);
			data.to_json(output);
			index++;
		}
		unmix(config, mix);
		return output;
	}

}