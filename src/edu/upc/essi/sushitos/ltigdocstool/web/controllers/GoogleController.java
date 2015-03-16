package edu.upc.essi.sushitos.ltigdocstool.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.log.SystemLogger;
import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;


/**
 * GoogleController class
 * Spring controller to process google callback
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
@Controller
public class GoogleController {
    
    protected final SystemLogger logger = SystemLogger.getSystemLogger();

    @RequestMapping(value = "/google/login.html", method = RequestMethod.GET)
    public ModelAndView storeToken(HttpServletRequest request, HttpServletResponse response) {
        
        String userId = request.getParameter("userId");
        GDocsManager gdocsMan;
        try {
            gdocsMan = new GDocsManager(userId, "userId");
            String token = gdocsMan.retrieveToken(request);
            AuthManager.storeToken(userId, token);
            return new ModelAndView("/google/login");    
        } catch (Exception e) {
           
        }
        logger.severe("/google/login.html");//TODO: Change logger call to log the errors
        return new ModelAndView("/error");
        
    }
    
    @RequestMapping(value = "/google/checkToken.html", method = RequestMethod.POST)
    public void checkToken(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("checkToken");
        response.setContentType("text/html");
        PrintWriter out;
        try {
            out = response.getWriter();
            
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
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
