/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The Class GridDataItem.
 */
public class GridDataItem extends DataItem {
	
	/** The row attributes. */
	protected HashMap<String,String> row_attrs = new HashMap<String, String>();
	
	/** The cell attributes. */
	protected HashMap<String,HashMap<String,String>> cell_attrs = new HashMap<String, HashMap<String,String>>();
	
	protected HashMap<String, String> userdata = new HashMap<String, String>();
	
	/**
	 * Instantiates a new grid data item.
	 * 
	 * @param data the data
	 * @param config the config
	 * @param index the index
	 */
	public GridDataItem(HashMap<String,String> data, DataConfig config, int index){
		super(data,config,index);
	}
	
	/**
	 * Sets the color of the row
	 * 
	 * @param color the new color
	 */
	public void set_row_color(String color){
		row_attrs.put("bgColor",color);
	}
	
	/**
	 * Sets the style of the row
	 * 
	 * @param style the new style
	 */
	public void set_row_style(String style){
		row_attrs.put("style",style);
	}
	
	/**
	 * Sets the style of the cell
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void set_cell_style(String name, String value){
		set_cell_attribute(name, "style", value);
	}
	
	/**
	 * Sets attribute of the cell 
	 * 
	 * @param name the name of column
	 * @param attr the attribute name
	 * @param value the value
	 */
	public void set_cell_attribute(String name, String attr, String value) {
		if (cell_attrs.get(name)==null){
			cell_attrs.put(name, new HashMap<String,String>());
		}
		cell_attrs.get(name).put(attr, value);
	}
	
	/**
	 * Sets attribute of row
	 * 
	 * @param name the name of attribute
	 * @param value the value
	 */
	public void set_row_attribute(String name, String value){
		row_attrs.put(name,value);
	}

	/**
	 * Sets css class for the cell
	 * 
	 * @param name the name of column
	 * @param value the value
	 */
	public void set_cell_class(String name,String value){
		set_cell_attribute(name, "class", value);
	}
	
	public void set_user_data(String name, String value){
		userdata.put(name, value);
	}
	
	public void set_userdata(String name, String value){
		set_user_data(name, value);
	}
	
	public String get_user_data(String name){
		return userdata.get(name);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataItem#to_xml_start(java.lang.StringBuffer)
	 */
	@Override
	public void to_xml_start(StringBuffer out) {
		out.append("<row id='");out.append(get_id());out.append("'");
		
		Iterator<String> it = row_attrs.keySet().iterator();
		while (it.hasNext()){
			String key = it.next().toString();
			out.append(" "); out.append(key); out.append("='"); out.append(row_attrs.get(key)); out.append("'");
		}
		out.append(">");

		for (int i=0; i<config.data.size(); i++){
			out.append("<cell");
			
			HashMap<String,String> current_cell_attrs = cell_attrs.get(config.data.get(i).name);
			if (current_cell_attrs!=null){
				Iterator<String> itc = current_cell_attrs.keySet().iterator();
				while (itc.hasNext()){
					String key_c= itc.next().toString();
					out.append(" ");
					out.append(key_c);
					out.append("='");
					out.append(current_cell_attrs.get(key_c));
					out.append("'");
				}
			}
			
			String value = get_value(config.data.get(i).name);
			out.append("><![CDATA[");
			if (value!=null) out.append(value);
			out.append("]]></cell>");
		}
		
		if (userdata!=null) {
			Iterator<String> uitc = userdata.keySet().iterator();
			while (uitc.hasNext()){
				String userdata_key = uitc.next().toString();
				out.append("<userdata name='"+userdata_key+"'><![CDATA[");
				out.append(userdata.get(userdata_key));
				out.append("]]></userdata>");
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataItem#to_xml_end(java.lang.StringBuffer)
	 */
	@Override
	public void to_xml_end(StringBuffer out) {
		out.append("</row>");
	}
	
}
