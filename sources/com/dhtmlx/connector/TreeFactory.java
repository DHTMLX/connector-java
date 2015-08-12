/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * A factory for creating Tree related objects.
 */
public class TreeFactory extends BaseFactory {
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	@Override
	public DataItem createDataItem(HashMap<String, String> data,
			DataConfig config, int index) {
		return new TreeDataItem(data, config, index);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataProcessor(com.dhtmlx.connector.BaseConnector, com.dhtmlx.connector.DataConfig, com.dhtmlx.connector.DataRequest, com.dhtmlx.connector.BaseFactory)
	 */
	@Override
	public DataProcessor createDataProcessor(BaseConnector connector,
			DataConfig config, DataRequest request,
			BaseFactory cfactory) {
		return new TreeDataProcessor(connector, config, request, cfactory);
	}
	
	public RenderStrategy createRenderStrategy() {
		return new TreeRenderStrategy();
	}
}
