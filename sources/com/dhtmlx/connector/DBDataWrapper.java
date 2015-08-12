/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class DBDataWrapper.
 * 
 * Class implements DataWrapper for common DB type
 */
public abstract class DBDataWrapper extends DataWrapper {
	
	/**
	 * Escape the data, befor using in SQL
	 * 
	 * @param data the incoming data
	 * 
	 * @return the escaped string
	 */
	public abstract String escape(String data);
	
	/**
	 * Gets the new id
	 * 
	 * @param result the resultset, which contains the new ID
	 * 
	 * @return the new id value
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public abstract String get_new_id(ConnectorResultSet result) throws ConnectorOperationException;
	
	
	/** The sequence name. */
	protected String sequence_name = "";
	
	/** The set of defined sql strings */
	private HashMap<OperationType,String> sqls = new HashMap<OperationType, String>();

	/**
	 * 
	 * Sets sequence
	 * 
	 * Sequence used in Oracle to implement auto-id generation
	 * 
	 * @param sequence_name the name of sequence
	 */
	public void sequence(String sequence_name){
		this.sequence_name = sequence_name;
	}
	
	/**
	 * Gets the connection.
	 * 
	 * @return the db connection
	 */
	protected Connection get_connection(){
		return (Connection)connection;
	}
	
	/**
	 * Attach.
	 * 
	 * @param name the name
	 * @param sql the sql
	 */
	public void attach(OperationType name, String sql){
		sqls.put(name,sql);
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#get_sql(com.dhtmlx.connector.OperationType, java.util.HashMap)
	 */
	public String get_sql(OperationType name, HashMap<String,String> data) throws ConnectorConfigException{
		String sql = sqls.get(name);
		if (sql==null) return "";
		
		//http://stackoverflow.com/questions/375420/java-equivalent-to-phps-pregreplacecallback/377484#377484
		StringBuffer result = new StringBuffer();
		Pattern regex = Pattern.compile("\\{([^}]+)\\}");
		Matcher regexMatcher = regex.matcher(sql);
		while (regexMatcher.find()) {
		  //String key = regexMatcher.group(1);
		  String value = data.get(regexMatcher.group(1));
		  if (value==null) throw new ConnectorConfigException("Unknown field in sql");
		  regexMatcher.appendReplacement(result, escape(value));
		}
		regexMatcher.appendTail(result);
		
		return result.toString();
	}
	
	public void new_order(DataAction data, DataRequest source) throws ConnectorOperationException {
		String order = source.get_order();
		if (order != null && !order.equals("")){
			String table = source.get_source();
			String id    = config.id.db_name;
			String idvalue = data.get_new_id();

			HashMap<String, String> max = query("SELECT MAX("+order+") as maxvalue FROM "+table).get_next();
			String maxvalue = Integer.toString( Integer.parseInt(max.get("maxvalue")) + 1 );

			query("UPDATE "+table+" SET "+order+" = "+maxvalue+" WHERE "+id+" = "+idvalue);
		}
	}
	
	public void order(DataAction data, DataRequest source) throws ConnectorOperationException {
		//id of moved item
		String id1 = escape(data.get_value("id"));
		//id of target item
		String target = data.get_value("target");
		
		String id2 = target; 
		boolean dropnext = false;
		if (target.indexOf("next:") == 0){
			dropnext = true;
			id2 = target.replace("next:", "");
		}

		id2 = escape(id2);
		
		//for tree like components we need to limit out queries to the affected branch only
		String relation_select = "";
		String relation_update = "";
		String relation_sql_out = "";
		String relation_sql = "";
		
		if (config.relation_id.name != null){
			String relation = data.get_value(config.relation_id.name);
			if (relation != null && !relation.equals("")){
				relation_sql = " "+config.relation_id.db_name + " = '" + escape(relation) + "' AND ";
				relation_select = config.relation_id.db_name + " as dhx_parent, ";
				relation_update = " "+config.relation_id.db_name + " = '"+ escape(relation) +"', ";
			}
		}
		
		String name = source.get_order();
		String table = source.get_source();
		String idkey = config.id.db_name;

		HashMap<String, String> sourceData = query("select "+relation_select+" "+name+" as dhx_index from "+table+" where "+idkey+" = '"+id1+"'").get_next();
		String source_index = sourceData.containsKey("dhx_index") ? sourceData.get("dhx_index") : "0";
		if (!relation_sql.equals(""))
			relation_sql_out = " " + config.relation_id.db_name + " = '" + escape(sourceData.get("dhx_parent")) +"' AND ";
		
		query("update "+table+" set "+name+" = "+name+" - 1 where "+relation_sql_out+" "+name+" >= "+source_index);

		String target_index = null;
		if (!id2.equals("")){
			HashMap<String, String> targetData = query("select "+name+" as dhx_index from "+table+" where "+idkey+" = '"+id2+"'").get_next();
			target_index = targetData.get("dhx_index");
			if (target_index == null || target_index.equals(""))
				target_index = "0";
			if (dropnext)
				target_index = Integer.toString( Integer.parseInt(target_index) + 1 );
			query("update "+table+" set "+name+" = "+name+" + 1 where "+relation_sql+" "+name+" >= "+target_index);
		} else {
			HashMap<String, String> targetData = query("select max("+name+") as dhx_index from "+table).get_next();
			target_index = targetData.get("dhx_index");
			if (target_index == null || target_index.equals(""))
				target_index = "1";
			else
				target_index = Integer.toString( Integer.parseInt(target_index) + 1 );
		}

		query("update "+table+" set "+relation_update+" "+name+" = "+target_index+" where "+idkey+" = '"+id1+"'");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#delete(com.dhtmlx.connector.DataAction, com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public void delete(DataAction data, DataRequest source) throws ConnectorOperationException {
		String sql = delete_query(data,source);
		query(sql);
		data.success();
	}

	/**
	 * Generate SQL for delete operation
	 * 
	 * @param data the data
	 * @param source the source
	 * 
	 * @return the sql string
	 */
	private String delete_query(DataAction data, DataRequest source) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ");
		sql.append(source.get_source());
		sql.append(" WHERE "+config.id.db_name+"='"+escape(data.get_id())+"'");
		
		String where = build_where(source.get_filters(), source.get_relation());
		if (!where.equals("")) 
			sql.append(" AND ( "+where+" )"); 
		return sql.toString();
	}
	
	/**
	 * Generate sql for insert query.
	 * 
	 * @param data the data
	 * @param source the source
	 * 
	 * @return the sql string
	 */
	protected String insert_query(DataAction data, DataRequest source) {
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();
		
		for (int i=0; i<config.text.size(); i++){
			if (i!=0){
				fields.append(",");
				values.append(",");
			}
			ConnectorField field = config.text.get(i);
			fields.append(field.db_name);

			if (data.get_value(field.name)==null)
				values.append("null");
			else
				values.append("'"+escape(data.get_value(field.name))+"'");
		}
		if (!config.relation_id.db_name.equals("")){
			fields.append(","+config.relation_id.db_name);
			values.append(",'"+escape(data.get_value(config.relation_id.name))+"'");
		}
		
		if (!sequence_name.equals("")){
			fields.append(","+config.id.db_name);
			values.append(","+sequence_name);
		}
		
		return "INSERT INTO "+source.get_source()+" ("+fields.toString()+")"+" VALUES ("+values.toString()+")";
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#get_size(com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public String get_size(DataRequest source) throws ConnectorOperationException {
		DataRequest count = new DataRequest(source);
		
		count.set_fieldset("COUNT(*) as DHX_COUNT ");
		count.set_sort(null);
		count.set_limit("","");
		
		ConnectorResultSet result = select(count);
		return result.get("DHX_COUNT");
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#get_variants(com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public ConnectorResultSet get_variants(DataRequest source, String field) throws ConnectorOperationException {
		DataRequest data = new DataRequest(source);
		data.set_fieldset("DISTINCT "+field + " AS value");
		data.set_sort(null);
		data.set_limit("","");
		
		return select(data);
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#insert(com.dhtmlx.connector.DataAction, com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public void insert(DataAction data, DataRequest source) throws ConnectorOperationException {
		String sql = insert_query(data,source);
		data.success(get_new_id(query(sql)));
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#select(com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public ConnectorResultSet select(DataRequest source) throws ConnectorOperationException {
		String select = source.get_fieldset();
		if (select.equals(""))
			select = config.db_names_list();
		
		String where = build_where(source.get_filters(), source.get_relation());
		String sort = build_order(source.get_sort_by());
		return query(select_query(select,source.get_source(), where, sort, source.get_start(), source.get_count()));
	}
	
	/**
	 * Generates sql for select query
	 * 
	 * @param select the list of fields
	 * @param from the name of table
	 * @param where the filtering rules
	 * @param sort the sorting rules
	 * @param start the start index
	 * @param count the count of records to fetch
	 * 
	 * @return the string
	 */
	protected String select_query(String select,String from,String where,String sort,String start,String count){
		if (from == "")
			return select;
		
		String sql="SELECT "+select+" FROM "+from;
		if (!where.equals("")) sql+=" WHERE "+where;
		if (!sort.equals("")) sql+=" ORDER BY "+sort;
		if (!start.equals("") || !count.equals("")) sql+=" LIMIT "+start+","+count;
		return sql;
	}	

	/**
	 * Convert filtering rules to sql string
	 * 
	 * @param rules the set of filtering rules
	 * @param relation the name of relation id field
	 * 
	 * @return the sql string
	 */
	protected String build_where(ArrayList<FilteringRule> rules, String relation){
		ArrayList<String> sql = new ArrayList<String>();
		for (int i=0; i<rules.size(); i++){
			sql.add(rules.get(i).to_sql(this));
		}
		if (!relation.equals(""))
			sql.add(config.relation_id.db_name+"='"+escape(relation)+"'");
		return ConnectorUtils.join(sql.toArray()," AND ");
	}
	
	/**
	 * Convert sorting rules to sql string
	 * 
	 * @param sorts the set of sorting rules
	 * 
	 * @return the sql string
	 */
	protected String build_order(ArrayList<SortingRule> sorts){
		ArrayList<String> sql = new ArrayList<String>();
		for (int i=0; i<sorts.size(); i++){
			sql.add(sorts.get(i).to_sql());
		}
		return ConnectorUtils.join(sql.toArray(),",");
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#update(com.dhtmlx.connector.DataAction, com.dhtmlx.connector.DataRequest)
	 */
	@Override
	public void update(DataAction data, DataRequest source) throws ConnectorOperationException {
		String sql = update_query(data,source);
		query(sql);
		data.success();
	}
	
	/**
	 * Builds the update sql query.
	 * 
	 * @param data the data
	 * @param source the source
	 * 
	 * @return the string
	 */
	private String update_query(DataAction data, DataRequest source) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE "+source.get_source()+" SET ");
		for (int i=0; i<config.text.size(); i++){
			if (i!=0) sql.append(",");
			
			ConnectorField field = config.text.get(i);
			sql.append(field.db_name+"=");
			if (data.get_value(field.db_name)==null)
				sql.append("null");
			else
				sql.append("'"+escape(data.get_value(field.db_name))+"'");
		}
		if (!config.relation_id.db_name.equals("")){
			if (config.text.size()>0)
				sql.append(",");

			sql.append(config.relation_id.db_name+"=");
			if (data.get_value(config.relation_id.name) == null)
				sql.append("null");
			else
				sql.append("'"+escape(data.get_value(config.relation_id.name))+"'");
		}
		
		sql.append(" WHERE "+config.id.db_name+"='"+escape(data.get_id())+"' ");
		//if we have limited set - set constraints
		String where = build_where(source.get_filters(),source.get_relation());
		if (!where.equals(""))
			sql.append(" AND ("+where+")");
		
		return sql.toString();
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#begin_transaction()
	 */
	public void begin_transaction() throws ConnectorOperationException{
		query("BEGIN");
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#commit_transaction()
	 */
	public void commit_transaction() throws ConnectorOperationException{
		query("COMMIT");
	}
	
	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.DataWrapper#rollback_transaction()
	 */
	public void rollback_transaction() throws ConnectorOperationException{
		query("ROLLBACK");
	}
	
	
	/**
	 * Gets the statement
	 * 
	 * @return the statement
	 * 
	 * @throws SQLException the SQL exception
	 */
	protected Statement getStatement() throws SQLException{
		return this.get_connection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}
	
	/**
	 * Executes DB query.
	 * 
	 * @param data the data
	 * 
	 * @return the connector result set
	 * 
	 * @throws ConnectorOperationException the connector operation exception
	 */
	public ConnectorResultSet query(String data)throws ConnectorOperationException{
		LogManager.getInstance().log("DB query \n"+data+"\n\n");
		try {
			Statement s = getStatement(); 	
			s.execute(data);
			
			ResultSet r = s.getResultSet();
			if (r!=null)
				if (!r.first()){
					r.close();
					r=null;
				}
			
			return new ConnectorResultSet(r, s);
		}
		catch (SQLException e){
			throw new ConnectorOperationException("Invalid SQL: "+data+"\n"+e.getMessage());
		}
	}
	
}
