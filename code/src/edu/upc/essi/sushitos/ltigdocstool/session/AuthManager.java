package edu.upc.essi.sushitos.ltigdocstool.session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.google.gdata.util.ServiceException;

import net.oauth.OAuthException;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.LTIRequestException;
import edu.upc.essi.sushitos.ltigdocstool.database.DatabaseManager;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.document.DuplicatedDocumentException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;

/**
 * AuthManager class
 * It manages sessions and authentication
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class AuthManager {

    private BLTILaunchRequest launchRequest;

    public AuthManager() {

    }

    public void createSession(HttpServletRequest request) throws LTIRequestException,
            OAuthException, ClassNotFoundException, SQLException, NeedCreateException, IOException,
            UnexistantOrganizationException {

        if (request.getParameter("lti_message_type") != null) {
            launchRequest = new BLTILaunchRequest(request);
            request.getSession().setAttribute("bltiRequest", launchRequest);
        } else {
            throw new LTIRequestException();
        }
    }

    public boolean isAuthorizedApp() throws ClassNotFoundException, SQLException,
            NeedCreateException, IOException {

        String userId = launchRequest.getToolConsumerInstanceGuid() + launchRequest.getUserId();
        DatabaseManager dbMan = new DatabaseManager();

        try {
            String token = dbMan.getUserToken(userId);
            GDocsManager gdocsMan = new GDocsManager(token, "token");
            gdocsMan.getDocumentLinkById("testing token");
        } catch (Exception e){
            return false;
        }

        return true;
    }

    public static String getToken(String userId) throws ClassNotFoundException, SQLException,
            NeedCreateException, IOException, UnexistantUserException {

        DatabaseManager dbMan = new DatabaseManager();
        String token = dbMan.getUserToken(userId);

        return token;
    }

    public static void storeToken(String userId, String token) throws ClassNotFoundException,
            SQLException, NeedCreateException, IOException {

        DatabaseManager dbMan = new DatabaseManager();
        try {
            dbMan.storeToken(userId, token);
        } catch (DuplicatedDocumentException e) {
            dbMan.updateToken(userId, token);
        }
    }

    public static void printError(PrintWriter w, Exception e) {

        w.println("<h1> Fuera de mi jardin!!!!</h1>");
        e.printStackTrace();
    }

    public static void printError(PrintWriter w) {

        w.println("<h1> Fuera de mi jardin!!!!</h1>");
    }

}
