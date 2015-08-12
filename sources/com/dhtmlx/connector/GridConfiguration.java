package com.dhtmlx.connector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GridConfiguration {
	private ArrayList<GridColumn> columns;
	private ArrayList<String[]> headers;
	private ArrayList<String[]> footers;
	
	public String delimiter = ",";
	
	public GridConfiguration(){
		columns = new ArrayList<GridColumn>();
		headers = new ArrayList<String[]>();
		footers = new ArrayList<String[]>();
	}
	
	public void addColumn(GridColumn column){
		columns.add(column);
	}
	public void addHeader(String[] columns){
		headers.add(columns);
	}
	public void addFooter(String[] columns){
		footers.add(columns);
	}
	private String join(String[] s){
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<s.length; i++){
        	if (i>0)
            	buffer.append(delimiter);
        	buffer.append(s[i]);
        }
        return buffer.toString();
    }
	public String toXML() {
		StringBuffer out = new StringBuffer();
		out.append("<head>");
		Iterator<GridColumn> order = columns.iterator();
		while (true){
			if (!order.hasNext()) break;
			GridColumn col = order.next();
			out.append(col.toXML());
		}
		
		out.append("<afterInit>");
		
		if (headers!=null){
			Iterator<String[]> horder = headers.iterator();
			while(true){
				if (!horder.hasNext()) break;
				String [] header = horder.next();
				out.append("<call command=\"attachHeader\"><param>");
				out.append(join(header));
				out.append("</param></call>");
			}
		}
		
		if (footers!=null){
			Iterator<String[]> forder = footers.iterator();
			while(true){
				if (!forder.hasNext()) break;
				String [] footer = forder.next();
				out.append("<call command=\"attachFooter\"><param>");
				out.append(join(footer));
				out.append("</param></call>");
			}
		}
		
		out.append("</afterInit>");
		out.append("</head>");
		
		return out.toString();
	}

	public String defineOptions(GridConnector conn) {
		if (!conn.is_first_call()) return null;
		Set<String> fill = new HashSet<String>();

		for (Integer i = 0; i < columns.size(); i++) {
			GridColumn col = columns.get(i);
			String type = col.getType();
			String name = col.getHeader();
			if (type.equals("co") || type.equals("coro") || name.equals("#connector_select_filter") || name.equals("#select_filter"))
				fill.add(i.toString());
		}
		
		for (Integer i = 0; i < headers.size(); i++) {
			String[] header = headers.get(i);
			for (Integer j = 0; j < header.length; j++) {
				if (header[j].equals("#connector_select_filter") || header[j].equals("#select_filter"))
					fill.add(j.toString());
			}
		}
		if (fill.size() == 0) return null;

		String[] colls = fill.toArray(new String[0]);
		String result = "";
		for (int i = 0; i < colls.length; i++) {
			result += colls[i];
			if (i < colls.length-1) result += ",";
		}
		return result;
	}
}
