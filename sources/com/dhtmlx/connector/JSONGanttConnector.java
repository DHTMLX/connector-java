package com.dhtmlx.connector;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;

public class JSONGanttConnector extends GanttConnector {

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 */
	public JSONGanttConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public JSONGanttConnector(Connection db, DBType db_type){
		this(db,db_type, new JSONGanttFactory());
	}
	
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public JSONGanttConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 * @param render_type the render class, which will be used to render items
	 */
	public JSONGanttConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
	}

	protected String xml_start() {
		return "{\"data\":";
	}

	protected String xml_end() {
		fill_collections();
		String end = (extra_output.length()>=0) ? extra_output.toString() : "";
		end += top_attributes_json() + "}";
		return end;
	}
	
	/**
	 * Render DB result set as JSONArray object
	 * 
	 * @param result the DB result
	 * 
	 * @return the json object
	 * 
	 * @throws ConnectorOperationException the connector operation exception 
	 */
	protected JSONArray render_json(ConnectorResultSet result) throws ConnectorOperationException{
		return render.render_json(result, cfactory, dynloading, "\n", config, mix);
	}

	protected String output_as_xml(ConnectorResultSet result) throws ConnectorOperationException{
		String xml = render_json(result).toJSONString();
		if (as_string)
			return xml;
		ConnectorOutputWriter out = new ConnectorOutputWriter(xml_start(), xml + xml_end());
		output_as_xml(out.toString());
		return out.toString();
	}

	public String output_as_xml(String data){
		if (as_string) return data;
 		http_response.reset();
		http_response.addHeader("Content-type", "text/javascript;charset=" + encoding);
		
		try {
			java.io.Writer out = http_response.getWriter();
			out.write(data);
			out.close();
			http_response.flushBuffer();
		} catch (IOException e){
			LogManager.getInstance().log("Error during data outputing");
			LogManager.getInstance().log(e.getMessage());
		}
		end_run();
		return "";
	}
	
	
	/**
	 * Fill collections of options with data from DB
	 */
	protected void fill_collections(){
		ArrayList<String> result = new ArrayList<String>();
		Iterator<String> key = options.keySet().iterator();
		while (key.hasNext()){
			String name = key.next();
			BaseConnector option_connector = options.get(name);
			String data = option_connector.render();
			result.add("\"" + name + "\":" + data);
		}
		String collections = ConnectorUtils.join(result.toArray(), ", ");
	    if (collections.length() > 0)
	    	collections = ",\"collections\": {" + collections + "}";

	    extra_output.append(collections);
	}


	/**
	 * Define connector for options retrieving 
	 * 
	 * @param name the name of column
	 * @param connector the connector
	 */
	public void set_options(String name, BaseConnector connector){
		options.put(name,connector);
	}
	
	/**
	 * Define fixed list of options
	 * 
	 * @param name the name of column
	 * @param object the iterable object ( array ) 
	 */
	@SuppressWarnings("unchecked")
	public void set_options(String name, Iterable object){
		Iterator it = object.iterator();
		StringBuffer data = new StringBuffer();
		
		while (it.hasNext()){
			String value = it.next().toString();
			data.append("<item value='"+value+"' label='"+value+"' />");
		}
		set_options(name,new DummyStringConnector(data.toString()));
	}
	
	/**
	 * Define fixed list of options
	 * 
	 * @param name the name of column
	 * @param object the hash object
	 */
	@SuppressWarnings("unchecked")
	public void set_options(String name, HashMap object){
		Iterator it = object.keySet().iterator();
		StringBuffer data = new StringBuffer();
		
		while (it.hasNext()){
			Object value = it.next();
			Object label = object.get(value).toString();
			data.append("<item value='"+value.toString()+"' label='"+label.toString()+"' />");
		}
		set_options(name,new DummyStringConnector(data.toString()));
	}
	
	@Override
	public void render_links(String table, String id, String fields) {
		JSONOptionsConnector links = new JSONOptionsConnector(get_connection(), db_type);
        links.render_table(table, id, fields);
        set_options("links", links);
	}

	@Override
    public String render() {
    	String mode = http_request.getParameter("gantt_mode");
		if (mode != null && mode.equals("links")){
			JSONOptionsConnector links = (JSONOptionsConnector)options.get("links");
			if (links != null){
				links.servlet(http_request, http_response);
				links.render_save();
				return null;
			}
    	}

    	return super.render();
    }
}
