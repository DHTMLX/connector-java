/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.io.IOException;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The Class ConnectorServlet.
 * 
 * Extends default servlet to simplify operations. 
 * Automatically assign request and response objects to the connector.
 */
public abstract class ConnectorServlet extends HttpServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6509163446916007821L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		BaseConnector.global_http_request=req;
		BaseConnector.global_http_response=res;
		try{
			configure();
		} finally {
			LogManager.getInstance().close();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		doGet(req,res);
	}
	
	/**
	 * Configure connector
	 * 
	 * This method need to be defined in result servlet
	 * Here, the actual connector instance must be created.  
	 */
	abstract protected void configure();
}
