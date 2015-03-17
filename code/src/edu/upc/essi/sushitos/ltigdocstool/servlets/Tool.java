package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.RoleManager;
import edu.upc.essi.sushitos.ltigdocstool.html.LearnerInterface;
import edu.upc.essi.sushitos.ltigdocstool.html.TeacherInterface;
import edu.upc.essi.sushitos.ltigdocstool.html.UserInterface;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;

/**
 * Servlet implementation class Tool
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class Tool extends HttpServlet {

    private static final long serialVersionUID = 1L;
    UserInterface ui = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tool() {

        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("DO GET TOOL");
        /**
         * Use the ServletRequest.getParameter(String name), getParameterMap(),
         * getParameterNames(), or getParameterValues() methods in the servlet's
         * doPost method
         */
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        BLTILaunchRequest launchRequest = (BLTILaunchRequest) request.getSession().getAttribute("bltiRequest");
        if (RoleManager.isLearner(launchRequest.getRoles())) {
            ui = new LearnerInterface();
            ui.printScreen(launchRequest, request, response);
        } else if (RoleManager.isTeacher(launchRequest.getRoles())) {
            ui = new TeacherInterface();
            ui.printScreen(launchRequest, request, response);
        } else {
            AuthManager.printError(out);
        }

        // out.println("<form action=\"/GoogleDocs/Tool\" method=\"post\">");
        // out.println("<input type=\"submit\" value=\"Send\"/>");
        // out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
    

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doGet(request, response);
    }
    
}
