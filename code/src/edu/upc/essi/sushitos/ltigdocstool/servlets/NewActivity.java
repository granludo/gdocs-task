package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.RoleManager;
import edu.upc.essi.sushitos.ltigdocstool.activity.ActivityManager;
import edu.upc.essi.sushitos.ltigdocstool.activity.DuplicatedActivityException;
import edu.upc.essi.sushitos.ltigdocstool.activity.UnexistantActivityException;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.html.LearnerInterface;
import edu.upc.essi.sushitos.ltigdocstool.html.TeacherInterface;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;

/**
 * Servlet implementation class NewActivity
 * 
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class NewActivity extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewActivity() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request.getSession().getAttribute("bltiRequest");
        if (RoleManager.isLearner(launchRequest.getRoles())) {
            out.println("ERROR: ACTIVITY NOT CONFIGURED");
        } else if (RoleManager.isTeacher(launchRequest.getRoles())) {
            out.println("TODO: ACTIVITY CONFIG FORM");
            String orgId = launchRequest.getToolConsumerInstanceGuid();
            String contextLabel = launchRequest.getContextLabel();
            String resourceLabel = launchRequest.getResourceLinkTitle();
            String resourceId = launchRequest.getResourceLinkId();
            
            try {
                ActivityManager.createActivity(orgId, contextLabel, resourceLabel, resourceId);
                out.println("<div><a href=\"Tool\">GO TO TOOL</a></div>");
            } catch (Exception e){
                AuthManager.printError(out, e);
            }
        } else {
            AuthManager.printError(out);
        }
        out.println("</body>");
        out.println("</html>");
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
