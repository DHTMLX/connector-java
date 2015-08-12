package com.dhtmlx.connector;

import java.util.HashMap;

public class FormFactory extends BaseFactory {
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	@Override
	public DataItem createDataItem(HashMap<String, String> data,
			DataConfig config, int index) {
		return new FormDataItem(data, config, index);
	}
}
