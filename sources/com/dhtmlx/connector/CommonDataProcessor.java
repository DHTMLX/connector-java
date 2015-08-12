package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class CommonDataProcessor extends DataProcessor {

	public CommonDataProcessor(BaseConnector connector, DataConfig config,
			DataRequest request, BaseFactory cfactory) {
		super(connector, config, request, cfactory);
	}

	@Override
	protected String get_list_of_ids() {
		if (((CommonConnector)connector).isSimpleProtocolUsed()){
			String id = connector.http_request.getParameter("id");
			if (id == null && connector.http_request.getParameter("action").equals("insert"))
				id = "dummy_insert_id";
			return id;
		} else
			return super.get_list_of_ids();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HashMap<String, HashMap<String, String>> get_post_values(
			String[] ids) {
		if (((CommonConnector)connector).isSimpleProtocolUsed()){
			HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
			HashMap<String, String> record = new HashMap<String, String>();
			Enumeration names = connector.http_request.getParameterNames();
			while(names.hasMoreElements()){
				String name = (String)names.nextElement();
				record.put(name, connector.http_request.getParameter(name));
			}
			for (int i=0; i<ids.length; i++)
				data.put(ids[i],record);
			return data;
		} else
			return super.get_post_values(ids);
	}

	@Override
	protected DataAction get_data_action(String status, String id,
			HashMap<String, String> itemData) {
		if (((CommonConnector)connector).isSimpleProtocolUsed())
			return new CommonDataAction(status, id, itemData);
		else
			return super.get_data_action(status, id, itemData);
	}

	@Override
	protected String output_as_xml(ArrayList<DataAction> result) {
		
		if (((CommonConnector)connector).isSimpleProtocolUsed()){
			StringBuffer out = new StringBuffer();
			for (int i=0; i<result.size(); i++)
				out.append(result.get(i).to_xml());
			return out.toString();
		} else {
			return super.output_as_xml(result);
		}
	}
	
	@Override
	protected String get_status(HashMap<String, String> itemData) {
		if (((CommonConnector)connector).isSimpleProtocolUsed()){
			return connector.http_request.getParameter("action");
		} else
			return super.get_status(itemData);
	}
	

}
