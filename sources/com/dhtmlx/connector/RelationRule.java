package com.dhtmlx.connector;

/**
 *	Class is used to create relation set between
 *	two connectors
 *	@param master_field
 *		field of master connector
 *	@param slave_field
 *		field of slave connector
 */
public class RelationRule {

	protected String master;
	protected String slave;
	
	public RelationRule(String slave_field, String master_field) {
		master = master_field;
		slave = slave_field;
	}
	
	public String getMaster() {
		return master;
	}
	
	public String getSlave() {
		return slave;
	}
	
}
