package com.dhtmlx.connector;

import java.util.HashMap;

public class FormDataItem extends DataItem {

	public FormDataItem(HashMap<String, String> data, DataConfig config,
			int index) {
		super(data, config, index);
	}
	@Override
	public void to_xml(StringBuffer out) {
		if (!skip){
			for (int i=0; i<config.text.size(); i++){
				String tag = config.text.get(i).name;
				out.append("<"+tag+"><![CDATA["+get_value(tag)+"]]></"+tag+">");
			}
		}
	}
}
