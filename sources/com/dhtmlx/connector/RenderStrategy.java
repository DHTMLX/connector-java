package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RenderStrategy {

	protected BaseConnector conn = null;
	protected Integer level = 0;
	protected Integer max_level = -1;
	protected String id_postfix = "__{group_param}";
	
	public void init(BaseConnector c) {
		conn = c;
	}
	
	
	/*! adds mix fields into DataConfig
	 *	@param config
	 *		DataConfig object
	 *	@param mix
	 *		mix structure
	 */
	protected void mix(DataConfig config, ArrayList<MixinRule> mix) {
		for (int i = 0; i < mix.size(); i++) {
			if (config.is_field(mix.get(i).getName())==-1) {
				try {
					config.add_field(mix.get(i).getName());
				} catch (ConnectorConfigException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*! remove mix fields from DataConfig
	 *	@param config
	 *		DataConfig object
	 *	@param mix
	 *		mix structure
	 */
	protected void unmix(DataConfig config, ArrayList<MixinRule> mix) {
		for (int i = 0; i < mix.size(); i++) {
			if (config.is_field(mix.get(i).getName())!=-1) {
				try {
					config.remove_field_full(mix.get(i).getName());
				} catch (ConnectorConfigException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*! adds mix fields in item
	 *	simple mix adds only strings specified by user
	 *	@param mix
	 *		mix structure
	 *	@param data
	 *		array of selected data
	 */
	protected HashMap<String,String> simple_mix(ArrayList<MixinRule> mix, HashMap<String,String> data) {
		// get mix details
		for (int i = 0; i < mix.size(); i++) {
			data.put(mix.get(i).getName(), mix.get(i).getValue());
		}
		return data;
	}

	/**
	 *  Adds mix fields in item
	 *	complex mix adds strings specified by user and results of subrequests
	 *	@param mix
	 *		mix structure
	 *	@param data
	 *		array of selected data
	 * @throws Exception 
	 */
	protected HashMap<String,Object> complex_mix(ArrayList<MixinRule> mix, HashMap<String,Object> data) throws Exception {
		// get mix details
		for (int i = 0; i < mix.size(); i++) {
			String mixname = mix.get(i).getName();
			if (mix.get(i).getConnector() != null) {
				BaseConnector subconn = mix.get(i).getConnector();
				ArrayList<RelationRule> filters = mix.get(i).get_filter().get_relations();
				// setting relationships
				subconn.clear_filters();
				for (int j = 0; j < filters.size(); j++) {
					RelationRule f = filters.get(j);
					if (data.containsKey(f.getMaster()))
						subconn.filter(f.getSlave(), data.get(f.getMaster()).toString());
					else
						throw new Exception("There was no such data field registered as: " + f.getMaster());
				}
				subconn.asString(true);
				JSONArray json = subconn.render_json();

				if (json.size()>1)
					data.put(mixname, json);
				else
					data.put(mixname, json.get(0));
			} else {
				data.put(mixname, mix.get(i).getValue());
			}
		}
		return data;
	}

	protected HashMap<String,Object> convert_to_json(HashMap<String,String> values) {
		HashMap<String,Object> data = new HashMap<String,Object>();
		for (Object key : values.keySet())
		    data.put(key.toString(), values.get(key));
		return data;
	}

	/**
	 * Render DB result set as XML string
	 * @param result - the DB result
	 * @param cfactory - the connector factory object
	 * @param dload - dynloading flag
	 * @param sep - items separator
	 * @return the xml string
	 * @throws ConnectorOperationException the connector operation exception 
	 */
	protected String render_set(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config, ArrayList<MixinRule> mix)
	throws ConnectorOperationException{
		StringBuffer output = new StringBuffer();
		int index = 0;
		HashMap<String,String> values;
		mix(config, mix);
		while ( (values = result.get_next()) != null){
			values = simple_mix(mix, values);
			DataItem data = cfactory.createDataItem(values, conn.config, index);
			if (data.get_id()==null)
				data.set_id(conn.uuid());

			conn.event.trigger().beforeRender(data);
			data.to_xml(output);
			output.append(sep);
			index++;
		}
		unmix(config, mix);
		return output.toString();
	}

	protected JSONArray render_json(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config, ArrayList<MixinRule> mix)
			throws ConnectorOperationException{
		// do nothing
		return null;
	}
	
	protected String parse_id(String id) {
		return parse_id(id, true);
	}

	protected String parse_id(String id, Boolean set_level) {
		String[] parts = id.split("%23|#");
		Integer level;
		if (parts.length == 2) {
			level = Integer.parseInt(parts[0]) + 1;
			id = parts[1];
		} else {
			level = 0;
			id = "";
		}
		if (set_level) this.level = level;
		return id;
	}

	/*! set maximum level of tree
	 * @param max_level
	 * 		maximum level
	 */
	public void set_max_level(int max) {
		max_level = max;
	}
	
	protected Integer get_level() {
		if (level > 0) return level;
		String parent = conn.http_request.getParameter(conn.parent_name);
		if (parent==null) {
			String ids = conn.http_request.getParameter("ids");
			if (ids!=null) {
				String[] aids = ids.split(",");
				if (aids.length > 0) {
					parse_id(aids[0]);
					level--;
				}
			}
			conn.request.set_relation("");
		} else {
			String id = parse_id(parent);
		}
		return level;
	}
	
	protected String level_id(String id) {
		return level_id(id, level);
	}

	protected String level_id(String id, Integer level) {
		return level.toString() + "%23" + id;
	}

	public String get_postfix() {
		return id_postfix;
	}
}
