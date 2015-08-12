package com.dhtmlx.connector;

public class GroupConnectorBehavior extends ConnectorBehavior {

	protected BaseConnector conn;

	public GroupConnectorBehavior(BaseConnector connector) {
		conn = connector;
	}
	
	public void beforeProcessing(DataAction action) {
		if (conn.http_request.getParameter("editing")!=null) {
			DataConfig config = conn.config;
			String pid = action.get_value(config.relation_id.name);
			pid = pid.replace(conn.render.get_postfix(), "");
			action.set_value(config.relation_id.name, pid);
			if (pid.length()==0 || pid.equals("0")) {
				action.error();
				action.set_response_text("This record can't be updated!");
			}
		}
	}
}
