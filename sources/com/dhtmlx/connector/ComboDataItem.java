/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * The Class ComboDataItem.
 * 
 * Class represents and option of the combobox
 */
public class ComboDataItem extends DataItem {
	
	/** The flag of selected option. */
	private boolean selected;
	
	/**
	 * Instantiates a new combo data item.
	 * 
	 * @param data the hash data
	 * @param config the data config
	 * @param index the index of option
	 */
	public ComboDataItem(HashMap<String, String> data, DataConfig config,
			int index) {
		super(data, config, index);
		selected = false;
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataItem#to_xml_start(java.lang.StringBuffer)
	 */
	@Override
	public void to_xml_start(StringBuffer out) {
		if (!skip){
			out.append("<option");
			if (selected)
				out.append(" selected='true'");
			out.append(" value='");
			out.append(xmlentities(get_id()));
			out.append(userdata_to_xml());
			out.append("' ><![CDATA[");
			out.append(get_value(config.text.get(0).name));
		}
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataItem#to_xml_end(java.lang.StringBuffer)
	 */
	@Override
	public void to_xml_end(StringBuffer out) {
		if (!skip)
			out.append("]]></option>");
	}
	
	/**
	 * Mark option as selected
	 */
	public void select(){
		selected = true;
	}
	
}
