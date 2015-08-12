/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * A factory for creating Combo related objects.
 */
public class ComboFactory extends BaseFactory {

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	@Override
	public DataItem createDataItem(HashMap<String, String> data,
			DataConfig config, int index) {
		return new ComboDataItem(data, config, index);
	}

}
