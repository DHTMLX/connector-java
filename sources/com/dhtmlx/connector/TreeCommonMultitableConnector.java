package com.dhtmlx.connector;

import java.sql.Connection;
import java.util.HashMap;

public class TreeCommonMultitableConnector extends TreeCommonConnector {

	protected Integer level = 0;
	protected Integer max_level = 0;

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 */
	public TreeCommonMultitableConnector(Connection db){
		this(db,DBType.Custom);
	}

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 */
	public TreeCommonMultitableConnector(Connection db, DBType db_type){
		this(db,db_type, new TreeCommonFactory());
	}

	/**
	 * Instantiates a new treecommonmultitable connector.
	 * 
	 * @param db the db connection
	 * @param db_type the db type
	 * @param a_factory the class factory, which will be used by object
	 */
	public TreeCommonMultitableConnector(Connection db, DBType db_type, BaseFactory a_factory){
		super(db,db_type,a_factory);
		event.attach(new TreeCommonMultitableBehavior(this));
	}
	
	protected void parse_request() {
		super.parse_request();

		String parent = http_request.getParameter("parent");
		if (parent!=null)
			request.set_relation(parse_id(parent));
		else
			request.set_relation("0");
	}
	
	/**
	 * Build xml response, based on previously provided configuration
	 * 
	 * All top level render methods, call this one, after parsing provided configuration
	 * 
	 * @return the xml string
	 */
	public String render(){
		parse_request();
		dynloading = true;
		if (editing){
			DataProcessor dp = cfactory.createDataProcessor(this, config, request, cfactory);
			String result;
			try {
				result = dp.process();
			} catch (ConnectorOperationException e) {
				LogManager.getInstance().log("Error during data processing");
				LogManager.getInstance().log(e.getMessage());
				result="<data>Operation error</data>";
			} catch (ConnectorConfigException e) {
				LogManager.getInstance().log("Error during configuration parsing");
				LogManager.getInstance().log(e.getMessage());
				result="<data>Configuration error</data>";
			}
			output_as_xml(result, true);
			return result;
		} else {
			event.trigger().beforeSort(request.get_sort_by());
			event.trigger().beforeFilter(request.get_filters());

			String parent = http_request.getParameter("parent");
			if (parent==null)
				request.set_relation("");

			try{
				return output_as_xml(sql.select(request));
			} catch(ConnectorOperationException e){
				e.printStackTrace();
				LogManager.getInstance().log("Error during data selecting");
				LogManager.getInstance().log(e.getMessage());
				return "";
			}
		}
	}


	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render_set(com.dhtmlx.connector.ConnectorResultSet)
	 */
	@Override
	protected String render_set(ConnectorResultSet result)
			throws ConnectorOperationException {

		StringBuffer output = new StringBuffer();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			if (values.get(config.id.name)==null) values.put(config.id.name, uuid());
			values.put(config.id.name, level_id(values.get(config.id.name)));
			TreeCommonDataItem data = (TreeCommonDataItem)cfactory.createDataItem(values, config, index);

			event.trigger().beforeRender(data);

			if (is_max_level()) {
				data.set_kids(-1);
			} else {
				if (data.has_kids()==-1)
					data.set_kids(1);
			}

			data.to_xml_start(output);
			data.to_xml_end(output);
			index++;
		}
		return output.toString();
	}

	public String xml_start() {
		String parent = http_request.getParameter("parent");
		if (parent!=null)
			add_top_attribute("parent", parent);
		else
			add_top_attribute("parent", "0");
		return "<data" + top_attributes() + ">";
	}

	public int get_level() {
		String parent = http_request.getParameter("parent");
		if (parent!=null)
			parse_id(parent);
		else {
			String ids = http_request.getParameter("ids");
			if (ids!=null) {
				String[] tmp = ids.split(",");
				if (tmp.length > 0)
					parse_id(tmp[0]);
				level -= 1;
			}
		}
		return level;
	}

	public String parse_id(String id) {
		String[] parts = id.split("#");
		if (parts.length == 2) {
			level = Integer.parseInt(parts[0]) + 1;
			id = parts[1];
		} else {
			level = 0;
			id = "";
		}
		return id;
	}

	protected String level_id(String id) {
		return level.toString() + '#' + id.toString();
	}

	/*! set maximum level of tree
		@param max_level
			maximum level
	*/
	public void set_max_level(int max_level) {
		this.max_level = max_level;
	}

	protected Boolean is_max_level() {
		if (level >= max_level)
			return true;
		else
			return false;
	}
}