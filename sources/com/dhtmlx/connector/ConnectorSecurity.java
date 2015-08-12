package com.dhtmlx.connector;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.xsshtmlfilter.*;;

public class ConnectorSecurity {

	public static int xss = ConnectorXSSMode.DHX_SECURITY_SAFETEXT;
	protected HTMLFilter filter = null;
	public static Boolean security_key = false;
	
	public String filter(String input) {
		return filter(input, xss);
	}
	
	public String filter(String input, int mode) {
		if (mode==ConnectorXSSMode.DHX_SECURITY_TRUSTED)
			return input;
		if (mode==ConnectorXSSMode.DHX_SECURITY_SAFEHTML)
			return softXSS(input);
		if (mode==ConnectorXSSMode.DHX_SECURITY_SAFETEXT)
			return hardXSS(input);
		return input;
	}

	protected String softXSS(String input) {
		if (filter==null)
			filter = new HTMLFilter();

		return filter.filter(input);
	}
	
	/**
	 * Splits html-tags
	 * @param input
	 * @return
	 */
	protected String hardXSS(String input) {
		return input.replaceAll("\\<.*?>","");
	}
	
	public static void CSRF_detected(HttpServletRequest req) throws ConnectorOperationException {
		StringBuffer message = new StringBuffer();
		message.append("[SECURITY] Possible CSRF attack detected\n");
		message.append("referer => " + req.getHeader("referer") + "\n");
		message.append("remote => " + req.getRemoteAddr());
		LogManager.getInstance().log(message.toString());
		throw new ConnectorOperationException("[SECURITY] Possible CSRF attack detected");
	}

	public static String checkCSRF(HttpServletRequest req, Boolean edit) throws ConnectorOperationException {
		HttpSession session = req.getSession(true);
		if (ConnectorSecurity.security_key){
            if (edit==true){
            	String update_key = req.getParameter("dhx_security");
            	Object master_key = session.getAttribute("dhx_security");
                if (update_key==null || !master_key.toString().equals(update_key)) {
                	ConnectorSecurity.CSRF_detected(req);
                    return "";
                }

                return "";
            }
            //data loading
            if (session.getAttribute("dhx_security")==null) {
            	session.setAttribute("dhx_security", UUID.randomUUID());
            }

            Object key = session.getAttribute("dhx_security");
            if (key==null) key="";
            return key.toString();
        }

        return "";
	}
}
