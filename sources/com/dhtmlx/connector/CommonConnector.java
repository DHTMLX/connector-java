package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;

public class CommonConnector extends BaseConnector {

	private boolean is_simple_protocol_used = false;
	protected HashMap<String,String> sections = null;

	public boolean isSimpleProtocolUsed(){
		return is_simple_protocol_used;
	}
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 */
	public CommonConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public CommonConnector(Connection db, DBType db_type){
		this(db,db_type, new CommonFactory());
	}
	
	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public CommonConnector(Connection db, DBType db_type, BaseFactory a_factory){
		this(db,db_type,a_factory,a_factory.createRenderStrategy());
	}

	/**
	 * Instantiates a new scheduler connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public CommonConnector(Connection db, DBType db_type, BaseFactory a_factory, RenderStrategy render_type){
		super(db,db_type,a_factory,render_type);
	}
	
	@Override
	protected void parse_request() {
		String action =  http_request.getParameter("action");
		String id =  http_request.getParameter("id");
		Boolean is_get_action = false;
		
		if (action!=null){
			String[] nameValuePairs = http_request.getQueryString().split("&");
			for(String nameValuePair: nameValuePairs) {
				if(nameValuePair.startsWith("action=")){
					is_get_action = true;
					break;
				}
			}
		}
  
		if (is_get_action){
			if (action.equals("get")  && id != null){
				this.request.set_filter(config.id.name, id);
			} else if (((action.equals("update") || action.equals("delete")) && id != null) || action.equals("insert")){
				editing = true;
				is_simple_protocol_used = true;
			} else {
				super.parse_request();		
			}
		} else
			super.parse_request();
		
	}


	public String getRecord(String getNewId) throws ConnectorOperationException {
		DataRequest source = new DataRequest(request);
		source.set_filter(config.id.name,getNewId,"=");
		
		ConnectorResultSet res = sql.select(source);
		return render_set(res);
	}
	
	public void add_section(String name, String value) {
		if (sections==null)
			sections = new HashMap<String, String>();
		sections.put(name, value);
	}
	
	protected String xml_start() {
		String start = super.xml_start();
		
		if (sections!=null) {
			for (Object key : sections.keySet()) {
			    Object value = sections.get(key);
			    String name = key.toString();
			    start += "<" + name + ">" + value.toString() + "</" + name + ">";
			}
		}
		return start;
	}
}
