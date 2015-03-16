package edu.upc.essi.sushitos.ltigdocstool.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.upc.essi.sushitos.configuration.ToolConfiguration;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.ltigdocstool.document.Document;
import edu.upc.essi.sushitos.ltigdocstool.document.DocumentManager;
import edu.upc.essi.sushitos.ltigdocstool.document.UnexistantDocument;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;

/**
 * @deprecated
 * TODO: TOBEDELETED
 *
 */
public class LearnerInterface extends UserInterface{
    
    public void printScreen(BLTILaunchRequest launchRequest,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<H1><b> Hey Mr. Learner </b></H1>");

        // Gather document data
        String org = launchRequest.getToolConsumerInstanceGuid();
        String contextLabel = launchRequest.getContextLabel();
        String resourceLabel = launchRequest.getResourceLinkTitle();
        String resourceId = launchRequest.getResourceLinkId();
        String userId = launchRequest.getUserId();
        String familyName = launchRequest.getLisPersonNameFamily();
        String givenName = launchRequest.getLisPersonNameGiven();
        Document doc = null;
        URL docLink = null;
        
        try {
            // Search for the document in the local database
            String userToken = AuthManager.getToken(org+userId);
            DocumentManager docMan = new DocumentManager(org+userId, userToken);
            doc = docMan.getDocument(org, contextLabel, resourceLabel, resourceId, org+userId);
            
            // Check for a Submit to professor action
            if (request.getParameter("submitdoc") != null) {
                doc = docMan.submit(doc, org, userId, resourceId);
            }
            
            // Add a "submit" button if the document is not already submitted
            if (!doc.getSubmitted()) {
                out.println("<form name=\"submitting\" action=\"\" method=\"post\">");
                out.println("<input type=\"submit\" name='submitdoc' value=\"Submit document\"/>");
                out.println("<input type=\"hidden\" name='id' value=\"" + doc.getId() + "\"/>");
                out.println("</form>");
            }
            
            // Ask for the document's link
            docLink = doc.getLink();
            
            // Open the document in an iframe
            out.println("<iframe src=\""+ docLink.toString() +"\" width=\"100%\" height=\"100%\" ></iframe>");
        } catch (UnexistantDocument e) {
            Document document;
            System.out.println("Document not found in local DB. Creating a new one.");
            try {
                // Create new document
                String userToken = AuthManager.getToken(org+userId);
                DocumentManager docMan = new DocumentManager(org+userId, userToken);
                document = docMan.createDocument(org, contextLabel, resourceLabel, resourceId, familyName, givenName, userId);
                docLink = document.getLink();

                // Add a "submit" button for submitting the document to the professor
                ToolConfiguration tc = ToolConfiguration.getToolConfiguration();
                String entrypoint = tc.getProperty("system", "entrypoint");
                
                out.println("<form action=\"" + entrypoint + "\" method=\"post\">");
                out.println("<input type=\"submit\" name='submitdoc' value=\"Submit document\"/>");
                out.println("<input type=\"hidden\" name='id' value=\"" + document.getId() + "\"/>");
                out.println("</form>");

                // Open the document in an iframe
                out.println("<iframe src=\""+ docLink.toString() +"\" width=\"100%\" height=\"100%\" ></iframe>");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
                
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}