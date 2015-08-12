package com.dhtmlx.connector;

import java.util.HashMap;
import java.util.Iterator;

public class TreeCommonDataItem extends CommonDataItem{

	public TreeCommonDataItem(HashMap<String, String> data, DataConfig config,
			int index) {
		super(data, config, index);
	}

	@Override
	public void to_xml_start(StringBuffer out) {
		out.append("<item id='" + get_id() + "' ");
		for (int i=0; i<config.data.size(); i++){
			String name = config.data.get(i).name;
			String value = get_value(name);
			if (value!=null) value = xmlentities(value);
			out.append(" " + name + "='" + value + "'");
		}
		if (has_kids()>0) out.append(" dhx_kids='1'");
		out.append(userdata_to_xml());
		out.append(">");
	}
	
	public void to_xml_end(StringBuffer out) {
		out.append("</item>");
	}

}
