package com.dhtmlx.connector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

public class ArrayConnectorResultSet extends ConnectorResultSet implements
		Iterable<Object> {

	protected Iterable<Object> result = null;
	protected Iterator<Object> it = null;

	public ArrayConnectorResultSet(Iterable<Object> data) {
		super(null, null);
		result = data;
		it = result.iterator();
	}

	/**
	 * Gets the next record in result set
	 * 
	 * @return the hash of data for the next record in result set
	 */
	public HashMap<String,String> get_next() {
		HashMap<String,String> data = new HashMap<String,String>();
		if (!it.hasNext()) return null;
		Object item = it.next();
		Field[] fields = item.getClass().getFields();
		try {
			for (Field field : fields)
				data.put(field.getName(), field.get(item).toString());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;
	}

	public Iterator<Object> iterator() {
		return result.iterator();
	}

}
