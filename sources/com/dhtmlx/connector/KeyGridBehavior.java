package com.dhtmlx.connector;

public class KeyGridBehavior extends ConnectorBehavior {

	private GridConnector grid;
	public KeyGridBehavior(GridConnector grid) {
		this.grid = grid;
	}
	
	@Override
	public void afterProcessing(DataAction action) {
		super.afterProcessing(action);
		
		String status = action.get_status(); 
		if (status.equals("inserted") || status.equals("updated")){ //update id of the row
			action.success(action.get_value(this.grid.config.id.name));
			action.set_status("inserted");
		}
	}

	@Override
	public void beforeProcessing(DataAction action) {
		String idvalue = action.get_value(this.grid.config.id.name);
		if (idvalue==null  || idvalue.equals("")){ //prevent empty key value
			action.error();
		}
		super.beforeProcessing(action);
	}

	
}
