package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.DuplicatedOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.Organization;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;

/**
 * Servlet implementation class NewOrg
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class NewOrg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewOrg() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Google Docs Tool</h1>");
		out.println("<h2>Register a new organization</h2>");
		out.println("<form method=\"POST\">");
		out.println("</p>Organization ID: ");
		out.println("<input type=\"text\" name=\"orgname\">");
		out.println("</p>");
		out.println("<p> Password: ");
		out.println("<input type=\"password\" name=\"password1\">");
		out.println("</p>");
		out.println("<p> Retype Password: ");
		out.println("<input type=\"password\" name=\"password2\">");
		out.println("</p>");
		out.println("<input type=\"submit\" name=\"submit\" value=\"Submit\">");
		out.println("</form>");
		out.println("</html>");
		out.println("</body>");	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST");
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Google Docs Tool</h1>");

		String orgId = request.getParameter("orgname");
		String password = request.getParameter("password1");
		String secondPassword = request.getParameter("password2");
		
		System.out.println(password);
		System.out.println(secondPassword);
		System.out.println(orgId);
		if (!password.equals(secondPassword)){
			out.println("<h2>Passwords don't match</h2>");
		} else {
			
			OrganizationManager orgManager = new OrganizationManager();
			try {
				orgManager.createNewOrganization(orgId, password);
				out.println("<h2>Successful registration</h2>");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NeedCreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicatedOrganizationException e) {
				// TODO Auto-generated catch block
				out.println("<h2>Your Organization was registered before</h2>");
			}
	
		}
		
		out.println("<a href=\"NewOrg\">Back</a>");
		out.println("</html>");
		out.println("</body>");	
	}

}
