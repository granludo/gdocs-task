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

/**
 * Servlet implementation class CheckToken
 * 
 * @deprecated
 * @TODO: TOBEDELETED
 */
public class CheckToken extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckToken() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String userId = request.getParameter("userId");
        System.out.println(userId);
        if (userId == null){
            return;
        }
        try {
            DatabaseManager dbMan = new DatabaseManager();
            String token = dbMan.getUserToken(userId);
            out.println("true");
        } catch (Exception e) {
            return;
        }
	}

}
