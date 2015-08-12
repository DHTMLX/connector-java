package com.dhtmlx.connector;
import java.util.HashMap;

public class MixedConnector extends BaseConnector {

	protected HashMap<String,BaseConnector> connectors;

	public MixedConnector() {
		super(null);
		connectors = new HashMap<String,BaseConnector>();
	}

	public void add(String name, BaseConnector conn) {
		connectors.put(name, conn);
	}

	public String render() {
		String result = "{";
		for (Object name : connectors.keySet()) {
		    BaseConnector value = connectors.get(name);
		    value.asString(true);
		    result += "\"" + name.toString() + "\":" + value.render() + ",";
		}
		result = result.substring(0, result.length()-1);

		result += "}";

		ConnectorOutputWriter out = new ConnectorOutputWriter(result, "");
		out.set_type(ExportServiceType.JSON);
		out.output(http_response, encoding);
		end_run();
		return "";
	}
}
