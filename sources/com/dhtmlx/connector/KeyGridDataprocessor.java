package com.dhtmlx.connector;

public class KeyGridDataprocessor extends GridDataProcessor {
	
	public KeyGridDataprocessor(BaseConnector connector, DataConfig config, DataRequest request, BaseFactory cfactory){
		super(connector,config,request,cfactory);
	}

	@Override
	public String name_data(String name) {
		if (name.equals("gr_id")) 
			return "__dummy__id__"; //ignore ID
		return super.name_data(name);
	}	
}
