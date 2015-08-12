package com.dhtmlx.connector;

import java.util.HashMap;

public class JSONTreeCommonFactory extends TreeCommonFactory {

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseFactory#createDataItem(java.util.HashMap, com.dhtmlx.connector.DataConfig, int)
	 */
	public DataItem createDataItem(HashMap<String,String> data, DataConfig config, int index){
		return new JSONTreeCommonDataItem(data,config,index);
	}
	
	public JSONTreeRenderStrategy createRenderStrategy() {
		return new JSONTreeRenderStrategy();
	}

}
