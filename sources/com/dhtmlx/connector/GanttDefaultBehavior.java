package com.dhtmlx.connector;

public class GanttDefaultBehavior extends ConnectorBehavior {
	GanttConnector master;
	GanttDefaultBehavior(GanttConnector master){
		this.master = master;
	}

	@Override
	public void afterDelete(DataAction action) {
		if (master.options.containsKey("links")){
			BaseConnector links = master.options.get("links");
			DBDataWrapper sql = (DBDataWrapper)master.sql;
			
			String table = links.request.get_source();
			String value = sql.escape(action.get_id());

			try {
				sql.query("DELETE FROM "+table+" WHERE source = '"+value+"'");
				sql.query("DELETE FROM "+table+" WHERE target = '"+value+"'");
			} catch (ConnectorOperationException e) {
				action.error();
			}
		}
		super.afterDelete(action);
	}
	
	@Override
	public void afterOrder(DataAction action) {
		DBDataWrapper sql = (DBDataWrapper)master.sql;
		
		String value  = action.get_id();
        String parent = action.get_value("parent");

        String table = master.request.get_source();
        String id    = master.config.id.db_name;

        try {
			sql.query("UPDATE "+table+" SET parent = "+parent+" WHERE "+id+" = "+value);
		} catch (ConnectorOperationException e) {
			action.error();
		}
	}
}
