package com.dhtmlx.connector;

import java.util.HashMap;

import org.json.simple.JSONArray;

public interface JSONDataItem extends DataItemInterface {

	public void set_data(HashMap<String,Object> data);
	public void to_json(JSONArray output);

}
