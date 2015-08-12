package com.dhtmlx.connector;

import java.util.HashMap;

public class CommonDataItem extends DataItem{

	public CommonDataItem(HashMap<String, String> data, DataConfig config,
			int index) {
		super(data, config, index);
	}

	@Override
	public void to_xml_start(StringBuffer out) {
		out.append("<item id='" + get_id() + "'");

		for (int i=0; i<config.data.size(); i++){
			String value = get_value(config.data.get(i).name);
			if (value==null) value="";
			out.append(" " + config.data.get(i).name + "='" + value + "'");
		}

		out.append(userdata_to_xml());
		out.append(" />");
	}
	
	public void to_xml_end(StringBuffer out) {};

}
