package com.dhtmlx.connector;

import java.util.HashMap;
import java.util.ArrayList;

public class MultitableTreeRenderStrategy extends TreeRenderStrategy {
	protected String sep = "#";

	public MultitableTreeRenderStrategy() {
		
	}
	
	public void init(BaseConnector c) {
		conn = c;
		conn.event.attach(new TreeCommonMultitableBehavior(conn));
	}
	
	public void set_separator(String value) {
		sep = value;
	}

	@Override
	protected String render_set(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config, ArrayList<MixinRule> mix)
			throws ConnectorOperationException {

		StringBuffer output = new StringBuffer();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			if (values.get(config.id.name)==null) values.put(config.id.name, conn.uuid());
			values.put(config.id.name, level_id(values.get(config.id.name)));
			DataItem data = cfactory.createDataItem(values, config, index);

			conn.event.trigger().beforeRender(data);

			if (is_max_level()) {
				data.set_kids(0);
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

	protected Boolean is_max_level() {
		if (level >= max_level)
			return true;
		else
			return false;
	}

}
