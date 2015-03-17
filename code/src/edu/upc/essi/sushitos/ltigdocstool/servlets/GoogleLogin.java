package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.authn.oauth.OAuthException;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;

/**
 * Servlet implementation class GoogleLogin
 * 
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class GoogleLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
        PrintWriter out = response.getWriter();
        
        String userId = request.getParameter("userId");
        GDocsManager gdocsMan;
        try {
            gdocsMan = new GDocsManager(userId, "userId");
            String token = gdocsMan.retrieveToken(request);
            AuthManager.storeToken(userId, token);
        } catch (Exception e) {
            AuthManager.printError(out, e);
        }
        
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println("<script type=\"text/javascript\">window.close ();</script>");
        out.println("</body>");
        out.println("</html>");
	}


}
