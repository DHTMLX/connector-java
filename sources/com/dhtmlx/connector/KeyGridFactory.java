package com.dhtmlx.connector;

public class KeyGridFactory extends GridFactory {

	@Override
	public DataProcessor createDataProcessor(BaseConnector connector,
			DataConfig config, DataRequest request, BaseFactory cfactory) {
		
		return new KeyGridDataprocessor(connector, config, request, cfactory);
	}

}
