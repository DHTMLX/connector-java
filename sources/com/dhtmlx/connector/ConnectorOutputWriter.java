package com.dhtmlx.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ConnectorOutputWriter {
	StringBuffer start;
	StringBuffer end;
	ExportServiceType type;
	private String name;
	private Boolean inline;
	private ByteArrayOutputStream bdata;
	public static Boolean clear_response = true;
	
	public ConnectorOutputWriter(String start, String end){
		this.start = new StringBuffer(start);
		this.end = new StringBuffer(end);
		type = ExportServiceType.XML;
	}
	
	public void add(String data){
		start.append(data);
	}
	
	public void add(ByteArrayOutputStream data){
		bdata = data;
	}
	
	public void reset(){
		start.delete(0, start.length());
		end.delete(0, end.length());
		bdata = null;
	}
	
	public void set_type(ExportServiceType type){
		this.type = type;
	}
	public void set_type(ExportServiceType type, String name, Boolean inline){
		this.type = type;
		this.name = name;
		this.inline = inline;
	}
	
	public void output(HttpServletResponse http_response, String encoding){
		if (clear_response) http_response.reset();
		try {
			if (type == ExportServiceType.XML || type == ExportServiceType.JSON){
				if (type == ExportServiceType.XML) {
					http_response.addHeader("Content-type", "text/xml;charset=" + encoding);
					start.insert(0, "<?xml version='1.0' encoding='"+encoding+"' ?>");
				}
//				if (type == ExportServiceType.JSON)
//					http_response.addHeader("Content-type", "text/json;charset=" + encoding);

			java.io.Writer out = http_response.getWriter();
			out.write(this.toString().toCharArray());
			out.close();
			http_response.flushBuffer();
				
			} else if (type == ExportServiceType.PDF){
				http_response.setContentType("application/pdf");
				asFile(http_response);
			} else if (type == ExportServiceType.Excel){
				http_response.setContentType("application/vnd.ms-excel");
				asFile(http_response);
			}
		} catch (IOException e){
			LogManager.getInstance().log("Error during data outputing");
			LogManager.getInstance().log(e.getMessage());
		}
	}
	
	private void asFile(HttpServletResponse http_response) throws IOException{
		http_response.addHeader("Content-Type","application/force-download");
		http_response.addHeader("Content-Type","application/octet-stream");
		http_response.addHeader("Content-Type","application/download");
		http_response.addHeader("Content-Transfer-Encoding","binary");
		
		http_response.addHeader("Content-Length",Integer.toString(this.getSize()));
		if (inline)
			http_response.addHeader("Content-Disposition","inline; filename=\""+name+"\";");
		else
			http_response.addHeader("Content-Disposition","attachment; filename=\""+name+"\";");
		
		ServletOutputStream out = http_response.getOutputStream();
		out.write(this.toBytes());
		out.flush();
		out.close();
	}
	
	private int getSize() {
		if (bdata != null)
			return bdata.size();
		return start.length() + end.length();
	}

	public String toString(){
		return start.toString()+end.toString();
	}
	public byte[] toBytes(){
		if (bdata != null)
			return bdata.toByteArray();
		return this.toString().getBytes();
	}
}
