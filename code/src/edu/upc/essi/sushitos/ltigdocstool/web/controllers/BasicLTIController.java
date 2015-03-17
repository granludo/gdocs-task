package edu.upc.essi.sushitos.ltigdocstool.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.LTIRequestException;
import edu.upc.essi.sushitos.log.SystemLogger;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;


/**
 * BasicLTIController class
 * Spring controller basicLTi related requests
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
@Controller
public class BasicLTIController {
    
    protected final SystemLogger logger = SystemLogger.getSystemLogger();
    
    private AuthManager authMan;
    
    @Autowired
    @Qualifier("authManager")
    public void setAuthManager(AuthManager newAuthMan){
        authMan = newAuthMan;
    }

    @RequestMapping(value = "/basicLTI/GoogleDocs.html", method = RequestMethod.POST)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        
        logger.info("/basicLTI/GoogleDocs.html");
        ModelAndView  view = new ModelAndView("/basicLTI/GoogleDocs");

        try {
            authMan.createSession(request);
        } catch (UnexistantOrganizationException uoe) {
            logger.warning("/basicLTI/GoogleDocs.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/basicLTI/newOrg");
        } catch (LTIRequestException e) {
            logger.warning("/basicLTI/GoogleDocs.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/index");
        } catch (OAuthException e) {
            logger.severe("/basicLTI/GoogleDocs.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/basicLTI/error");
        } catch (NeedCreateException e) {
            logger.severe("/basicLTI/GoogleDocs.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/error");
        } catch (Exception e) {
            logger.severe("/basicLTI/GoogleDocs.html");//TODO: Change logger call to log the errors
            return new ModelAndView("/error");
        }

        // AUTENTICATE AT GOOGLE
        try {
//            out.println("<h1>WELCOME</h1>");
            BLTILaunchRequest launchRequest = (BLTILaunchRequest) request.getSession()
                    .getAttribute("bltiRequest");
            if (!authMan.isAuthorizedApp()) {
//                out.println("TODO: Some info and instructions");
                String userId = launchRequest.getToolConsumerInstanceGuid()
                        + launchRequest.getUserId();
                GDocsManager gdocsMan = new GDocsManager(userId, "userId");
                String approvalPageUrl = gdocsMan.getAuthorizationLink();
                view = new ModelAndView("/basicLTI/approval");
                view.addObject("approvalLink", approvalPageUrl);
                view.addObject("userId", userId);
            } else {
                view = new ModelAndView("redirect:/activity/viewActivity.html");
            }

//            out.println("<div id=\"wait\" class=\"hiddenwait\"><img src=\"resources/wait_spinning.gif\" /></div>");
//            out.println("</body>");
//            out.println("</html>");
        } catch (Exception e) {
//            AuthManager.printError(out, e);
        }
        return view;
    }

}
