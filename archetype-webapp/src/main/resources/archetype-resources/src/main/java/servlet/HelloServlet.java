/******************************************************************************
*
* Copyright: Intellectual Property of Four Elements Capital Pte Ltd, Singapore.
* All rights reserved.
*
******************************************************************************/

package ${package}.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloServlet extends HttpServlet {
	
	private Logger log = LogManager.getLogger(HelloServlet.class.getName());
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {      
			
		String str = "Hello four elements capital !";

		log.debug(">>> String result : " + str);	
		
		PrintWriter out = response.getWriter();
		out.println(str);
		out.close();		
	}

}

 