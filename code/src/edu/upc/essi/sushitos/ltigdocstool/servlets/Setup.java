package edu.upc.essi.sushitos.ltigdocstool.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedUpgradeException;

/**
 * Servlet implementation class Setup
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class Setup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Setup() {
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
		out.println("<h1>Google Docs Tool Setup</h1>");
		
		DatabaseManager dbManager;
		try {
			 dbManager = new DatabaseManager();
			 if (!dbManager.isDatabaseCreated()){
				 dbManager.createDatabase();
				 out.println("<p>Installation Success</p>");
			 } else {
				 out.println("<p>There is no need to upgrade</p>");
			 }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NeedCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NeedUpgradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		out.println("</html>");
		out.println("</body>");
	}


}
