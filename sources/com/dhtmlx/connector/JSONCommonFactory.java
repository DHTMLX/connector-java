package com.dhtmlx.connector;

import java.util.HashMap;

public class JSONCommonFactory extends CommonFactory {
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	public DataItem createDataItem(HashMap<String,String> data, DataConfig config, int index){
		return new JSONCommonDataItem(data,config,index);
	}
	
	public RenderStrategy createRenderStrategy() {
		return new JSONRenderStrategy();
	}
}
