/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * The Class GanttDataItem.
 */
public class GanttDataItem extends DataItem {
	
	/**
	 * Instantiates a new scheduler data item.
	 * 
	 * @param data the data
	 * @param config the config
	 * @param index the index
	 */
	public GanttDataItem(HashMap<String,String> data, DataConfig config, int index){
		super(data,config,index);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataItem#to_xml(java.lang.StringBuffer)
	 */
	@Override
	public void to_xml(StringBuffer out) {
		if (!skip){
		
			out.append("<task id='"+get_id()+"' >");
			out.append("<start_date><![CDATA["+get_value(config.text.get(0).name)+"]]></start_date>");
			
			String second = config.text.get(1).name;
			out.append("<"+second+"><![CDATA["+get_value(second)+"]]></"+second+">");
			
			out.append("<text><![CDATA["+get_value(config.text.get(2).name)+"]]></text>");
			
			for (int i=3; i<config.text.size(); i++){
				String tag = config.text.get(i).name;
				out.append("<"+tag+"><![CDATA["+get_value(tag)+"]]></"+tag+">");
			}
			
			out.append(userdata_to_xmltag());
			out.append("</task>");
		}
	}
	
}
