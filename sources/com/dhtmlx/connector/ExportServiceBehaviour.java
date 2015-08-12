package com.dhtmlx.connector;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExportServiceBehaviour extends ConnectorBehavior {
	private String url;
	private ExportServiceType type;
	protected String name;
	protected Boolean inline;
	public ExportServiceBehaviour(String url, ExportServiceType type){
		super();
		this.url = url;
		this.type = type;
		this.inline = false;
		if (this.type == ExportServiceType.PDF)
			this.name = "report.pdf";
		else
			this.name = "report.xls";
	}
	@Override
	public void beforeOutput(ConnectorOutputWriter out, HttpServletRequest http_request, HttpServletResponse http_response) {
		
			String temp_data = out.toString().replace("<head>", "<head><columns>").replace("</head>", "</columns></head>");
			
		try {
			URL conversion = new URL(url);
			HttpURLConnection process = (HttpURLConnection) conversion.openConnection();
			
			process.setDoInput(true);
			process.setDoOutput(true);
			process.setUseCaches(false);
			process.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			   
			    
			OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
			
			
			writer.write(URLEncoder.encode("grid_xml","UTF-8")+"="+URLEncoder.encode(temp_data,"UTF-8"));
			writer.flush();
			writer.close();
			process.connect();
			
			InputStream is = process.getInputStream();
			ByteArrayOutputStream data = new ByteArrayOutputStream();
            byte[] byteChunk = new byte[4096];
            
            int n;
            while ( (n = is.read(byteChunk)) > 0 ) {
              data.write(byteChunk, 0, n);
            }

                                        
            is.close();
            
			out.reset();
			out.add(data);
			out.set_type(type, name, inline);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		super.beforeOutput(out, http_request, http_response);
	}
	
	
}
