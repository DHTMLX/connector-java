package com.dhtmlx.connector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayDBDataWrapper extends DBDataWrapper {

	protected ArrayConnectorResultSet data;

	public ArrayDBDataWrapper(Iterable<Object> data) {
		this.data = new ArrayConnectorResultSet(data);
	}

	@Override
	public ConnectorResultSet select(DataRequest source) throws ConnectorOperationException {
		ArrayList<Object> result = new ArrayList<Object>();

		if (config.relation_id == null || config.relation_id.db_name.equals("")) {
			if (source.get_relation().equals("0") || source.get_relation().equals("")) {
				return data;
			} else {
				return new ArrayConnectorResultSet(result);
			}
		}

		String relation_id = config.relation_id.db_name;

		Iterator it = data.iterator();
		while (it.hasNext()) {
			Object item = it.next();
			try {
				Field f = item.getClass().getField(relation_id);
				Object value = f.get(item);
				if (value == null) continue;
				if (value.toString().equals(source.get_relation()))
					result.add(item);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return new ArrayConnectorResultSet(result);
	}

	@Override
	public String escape(String data) {
		return null;
	}

	@Override
	public String get_new_id(ConnectorResultSet result)
			throws ConnectorOperationException {
		return null;
	}

}