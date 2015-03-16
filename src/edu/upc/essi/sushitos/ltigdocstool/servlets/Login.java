package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.ltigdocstool.activity.ActivityManager;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;

/**
 * Servlet implementation class Login
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {

        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO Auto-generated method stub
        System.out.println("DO GET LOGIN");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>WELCOME</h1>");
        out.println("TODO: Some info and instructions");
        out.println("<div><a href=\"NewOrg\">Registration</a></div>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO Auto-generated method stub

        System.out.println("DO POST LOGIN");
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<html>");
        out.println("<head>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/styles.css\" />");
        out.println("<script type=\"text/javascript\" src=\"resources/jquery-1.4.4.min.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"resources/grayout.js\"></script>");
        out.println("</head>");
        out.println("<body>");
        AuthManager authMan = null;
        authMan = new AuthManager();

        try {
            authMan.createSession(request);
        }catch (UnexistantOrganizationException uoe){
            out.println("TODO: Some info and instructions");
            out.println("<div><a href=\"NewOrg\">Registration</a></div>");
            out.println("</body>");
            out.println("</html>");
            return;
        } catch (Exception e) {
            
            AuthManager.printError(out, e);
        }

        // AUTENTICATE AT GOOGLE

        try {
            out.println("<h1>WELCOME</h1>");
            BLTILaunchRequest launchRequest = (BLTILaunchRequest) request.getSession()
            .getAttribute("bltiRequest");
            if (!authMan.isAuthorizedApp()) {
                out.println("TODO: Some info and instructions");
                String userId = launchRequest.getToolConsumerInstanceGuid()
                        + launchRequest.getUserId();
                GDocsManager gdocsMan = new GDocsManager(userId, "userId");
                String approvalPageUrl = gdocsMan.getAuthorizationLink();
                out.println("<div><a href=\"" + approvalPageUrl + "\" target=\"_blank\" onclick=\"grayOut(true);checkToken('"+userId+"');\">POPUP LINK</a></div>");
            } else if (!ActivityManager.isConfigured(launchRequest.getToolConsumerInstanceGuid(), launchRequest.getContextLabel(), launchRequest.getResourceLinkTitle(), launchRequest.getResourceLinkId())){
                response.sendRedirect("NewActivity");
            } else {
                response.sendRedirect("Tool");
            }
            
            out.println("<div id=\"wait\" class=\"hiddenwait\"><img src=\"resources/wait_spinning.gif\" /></div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            AuthManager.printError(out, e);
        }

    }

}
