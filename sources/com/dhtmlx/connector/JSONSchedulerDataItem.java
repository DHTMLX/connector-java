package com.dhtmlx.connector;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONSchedulerDataItem extends SchedulerDataItem implements JSONDataItem {

	protected HashMap<String,Object> data;

	public JSONSchedulerDataItem(HashMap<String, String> data, DataConfig config, int index) {
		super(data, config, index);
	}

	public void set_data(HashMap<String,Object> data) {
		this.data = data;
	}
	
	/**
	 * Gets the value of the field
	 * 
	 * @param name the name of the field
	 * 
	 * @return the value of the field
	 */
	protected Object get_json_value(String name){
		return data.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public void to_json(JSONArray output) {
		if (!skip) {
			JSONObject record = new JSONObject();

			record.put("id", get_id());
			record.put("start_date", get_value(config.data.get(0).name));
			record.put("end_date", get_value(config.data.get(1).name));
			record.put("text", get_value(config.data.get(2).name));
			for (int i=3; i<config.data.size(); i++)
				record.put(config.data.get(i).name, get_json_value(config.data.get(i).name));

			userdata_to_json(record);
			output.add(record);
		}
	}

}
