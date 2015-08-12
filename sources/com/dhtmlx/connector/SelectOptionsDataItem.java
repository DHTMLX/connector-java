package com.dhtmlx.connector;

import java.util.HashMap;

public class SelectOptionsDataItem extends DataItem{

	public SelectOptionsDataItem(HashMap<String, String> data, DataConfig config,
			int index) {
		super(data, config, index);
	}

	@Override
	public void to_xml_start(StringBuffer out) {
		out.append("<item");
		
		out.append(" ");
		out.append("value='");
		out.append(xmlentities(get_value(config.data.get(0).name)));
		out.append("'");

		out.append(" ");
		out.append("label='");
		out.append(xmlentities(get_value(config.data.get(1).name)));
		out.append("'");			
		
		out.append(">");
	}
}