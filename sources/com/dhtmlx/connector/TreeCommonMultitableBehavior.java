package com.dhtmlx.connector;

public class TreeCommonMultitableBehavior extends ConnectorBehavior {

	protected BaseConnector conn;

	/**
	 * Instantiates a new connector behavior.
	 */
	public TreeCommonMultitableBehavior(BaseConnector connector){
		super();
		conn = connector;
	}

	// id translation before
	public void beforeProcessing(DataAction action) {
		DataConfig config = conn.config;
		String id = action.get_id();
		id = conn.render.parse_id(id, false);
		action.set_id(id);
		action.set_value("tr_id", id);
		action.set_new_id(id);
		String pid = action.get_value(config.relation_id.db_name);
		pid = conn.render.parse_id(pid, false);
		action.set_value(config.relation_id.db_name, pid);
	}

	// id translation after
	public void afterProcessing(DataAction action) {
		String id = action.get_id();
		action.set_id(conn.render.level_id(id));
		id = action.get_new_id();
		id = conn.render.level_id(id);
		action.success(id);
	}

}
