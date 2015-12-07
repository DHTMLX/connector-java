/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.io.IOException;
import java.util.Date;

/**
 * The Class LogManager.
 */
public class LogManager {
	
	/** The instance of class */
	private static LogManager instance; 
	
	/**
	 * Gets the single instance of LogManager.
	 * 
	 * @return single instance of LogManager
	 */
	public static LogManager getInstance(){
		if (instance == null)
			instance = new LogManager();
		return instance;
	}
	
	/** The logging flag */
	private boolean enabled = false;
	
	/** The client logging flag */
	public boolean client_log = false;
	
	/** The log writer. */
	private java.io.Writer log_writer;
	
	/** The session log */
	private StringBuffer session = new StringBuffer();
	
	/**
	 * Enables log writing
	 * 
	 * @param writer the log writer
	 * @param client the client logging flag
	 */
	public void enable_log(java.io.Writer writer, boolean client){
		if (log_writer!=null)
			try {
				log_writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		log_writer = writer;
		client_log = client;
		enabled = writer!=null;
		
		log("\n\n====================================\nLog started, "+(new Date()).toString()+"\n====================================");
			
	}
	
	/**
	 * Enables logging
	 * 
	 * @param writer the log writer
	 */
	public void enable_log(java.io.Writer writer){
		enable_log(writer,false);
	}
	
	/**
	 * Log data
	 * 
	 * @param data the data
	 */
	public void log(String data){
		if (enabled)
			try {
				session.append("\n"+data);
				log_writer.write("\n"+data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
	}
	
	/**
	 * Log data
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public void log(String name, String value){
		if (enabled)
			log(name+" : "+value);
	}
	
	/**
	 * Gets session log.
	 * 
	 * @return the session log
	 */
	public String get_session_log(){
		return session.toString();
	}
	
	/**
	 * Close log writer
	 */
	public void close(){
		if (log_writer == null) return;
		
		try {
			log_writer.flush();
			log_writer.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize(){
		close();
	}

}
