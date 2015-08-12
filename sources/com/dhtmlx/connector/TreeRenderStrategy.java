package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeRenderStrategy extends RenderStrategy {

	public void init(BaseConnector c) {
		conn = c;
		conn.event.attach(new TreeGridBehavior(conn.config));
	}

	/* (non-Javadoc)
	 * @see com.dhtmlx.connector.BaseConnector#render_set(com.dhtmlx.connector.ConnectorResultSet)
	 */
	@Override
	protected String render_set(ConnectorResultSet result, BaseFactory cfactory, Boolean dload, String sep, DataConfig config, ArrayList<MixinRule> mix)
			throws ConnectorOperationException {

		StringBuffer output = new StringBuffer();
		int index = 0;
		HashMap<String,String> values;
		while ( (values = result.get_next()) != null){
			values = simple_mix(mix, values);
			DataItem data = cfactory.createDataItem(values, conn.config, index);
			if (data.get_id()==null)
				data.set_id(conn.uuid());

			conn.event.trigger().beforeRender(data);

			if (data.has_kids()==-1 && dload)
				data.set_kids(1);

			mix(config, mix);
			data.to_xml_start(output);
			unmix(config, mix);

			if (data.has_kids()==-1 || ( data.has_kids()!=0 && !dload)){
				DataRequest sub_request = new DataRequest(conn.request);
				sub_request.set_relation(data.get_id());
				output.append(render_set(conn.sql.select(sub_request), cfactory, dload, sep, new DataConfig(config), mix));
			}
			data.to_xml_end(output);
			index++;
		}
		result.close();
		return output.toString();
	}

}
