package com.dhtmlx.connector;

import java.util.HashMap;

public class CommonDataAction extends DataAction {

	public CommonDataAction(String status, String id,
			HashMap<String, String> data) {
		super(status, id, data);
	}

	@Override
	public String to_xml() {
		if (get_status() == "error" || get_status() == "invalid")
			return "false";
		else 
			return "true"+(get_status().equals("insert")?("\n"+get_new_id()):"");
	}
	
}
