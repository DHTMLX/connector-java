package com.dhtmlx.connector;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ThreadSafeConnectorServlet extends HttpServlet {

	private static final long serialVersionUID = 8532251727722723922L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		try{
			configure(req,res);
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
	abstract protected void configure(HttpServletRequest req, HttpServletResponse res);
}
