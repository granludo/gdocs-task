package edu.upc.essi.sushitos.ltigdocstool.html;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.upc.essi.sushitos.google.docs.GDocsManager;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.lis.Lis;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.document.Document;
import edu.upc.essi.sushitos.ltigdocstool.document.DocumentManager;
import edu.upc.essi.sushitos.ltigdocstool.session.AuthManager;
import edu.upc.essi.sushitos.ltigdocstool.session.UnexistantUserException;

/**
 * @deprecated
 * TODO: TOBEDELETED
 */
public class TeacherInterface extends UserInterface{
	
	public void printScreen(BLTILaunchRequest launchRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<H1><b> Hey Mr. Teacher </b></H1>");
		printDocumentsList(out, launchRequest, request);
		
		if (request.getParameter("opendocument") != null) {
			printIframe(out, launchRequest, request);
		}
	}
	
	private void printGradingButton(PrintWriter out,
			BLTILaunchRequest launchRequest, HttpServletRequest request, int num, String userId, String orgId)
			throws MalformedURLException {
		
		String gradingMessage = "";
		if ((request.getParameter("showgradeform") != null) &&
					(Integer.parseInt(request.getParameter("docnumber")) == num)){
			printGradingForm(out, launchRequest, request);
			return;
		}
	
		if ((request.getParameter("submitgrade") != null)  &&
		(Integer.parseInt(request.getParameter("docnumber")) == num)){	
			gradingMessage = gradeOperation(out, launchRequest, request);
			out.println(gradingMessage);
		}
	
		out.println("<form name=\"grading\" method=\"post\" action=\"\">");
		out.println("<input type=\"submit\" name='showgradeform' value=\"Grade\"/>");
		out.println("<input type=\"hidden\" name='docnumber' value=\"" + num + "\"/>");
		out.println("<input type=\"hidden\" name='userId' value=\"" + userId + "\"/>");
		out.println("<input type=\"hidden\" name='orgId' value=\"" + orgId + "\"/>");
		out.println("</form>");
	}
	
	private void printGradingForm(PrintWriter out,
			BLTILaunchRequest launchRequest, HttpServletRequest request)
			throws MalformedURLException {
		
		String userId = request.getParameter("userId");
		String orgId = request.getParameter("orgId");
		String num = request.getParameter("docnumber");
		
		out.println("<p>");
		out.println("<form action=\"\" name=\"GradesForm\" id=\"GradesForm\" method=\"post\">");
	
		// TODO DEBUG: The 3 following lines should be removed at some point
//		out.println("Service URL: <input type=\"text\" name=\"url\" size=\"40\" disabled=\"true\" value=\""
//				+ launchRequest.getExtImsLisBasicOutcomeUrl() + "\"/></br>");
//		out.println("OAuth Consumer Key: <input type=\"text\" name=\"key\" disabled=\"true\" size=\"40\" value=\""
//				+ launchRequest.getOauthConsumerKey() + "\"/></br>");
//		out.println("lis_result_sourcedid: <input type=\"text\" name=\"sourcedid\" disabled=\"true\" size=\"100\" value=\""
//				+ launchRequest.getLisResultSourcedid() + "\"/></br>");
	
		out.println("</p><p>");
		if (request.getParameter("grade") != null) {
			out.println("Grade to Send to LMS: <input type=\"text\" name=\"grade\" value=\""
					+ request.getParameter("grade") + "\"/>");
		} else {
			out.println("Grade to Send to LMS: <input type=\"text\" name=\"grade\" value=\"\"/>");
		}
		out.println("(i.e. 0.95)<br/>");
		out.println("<input type='submit' name='submitgrade' value=\"SendGrade\">");
		out.println("<input type='submit' name='submitgrade' value=\"DeleteGrade\">");
		out.println("<input type='submit' name='submitgrade' value=\"ReadGrade\"></br>");
		out.println("<input type=\"hidden\" name='userId' value=\"" + userId + "\"/>");
		out.println("<input type=\"hidden\" name='orgId' value=\"" + orgId + "\"/>");
		out.println("<input type=\"hidden\" name='docnumber' value=\"" + num + "\"/>");
		out.println("</form>");
		out.println("<p>");
		out.println("<b>Note</b> Because this is a server-to-server call if you are");
		out.println("running your LMS on \"localhost\", you must also run this script");
		out.println("(setoutcome.php) on localhost as well.  If your LMS has a real Internet");
		out.println("address you should be OK.");
		out.println("</p>");
	}
	
	private void printDocumentsList(PrintWriter out,
			BLTILaunchRequest launchRequest, HttpServletRequest request) {
		List<Document> courseDocuments = new LinkedList<Document>();
	
		String org = launchRequest.getToolConsumerInstanceGuid();
		String contextLabel = launchRequest.getContextLabel();
		String resourceLabel = launchRequest.getResourceLinkTitle();
		String resourceId = launchRequest.getResourceLinkId();
		String userId = launchRequest.getUserId();
        
		String userToken = null;
        try {
            userToken = AuthManager.getToken(org+userId);
            DocumentManager docMan = new DocumentManager(org+userId, userToken);
            courseDocuments = docMan.getDocuments(org, contextLabel,
                    resourceLabel, resourceId);
            ListIterator<Document> it = (ListIterator<Document>) courseDocuments
                    .listIterator();
            int num = 1;
            out.println("<p>");
            out.println("<ol>");
            while (it.hasNext()) {
                Document doc;
                doc = it.next();
                out.println("<li>");
                if (doc.getSubmitted()) {
                    out.print("<span style=\"color:green\">Submitted </span>");
                }
    			GDocsManager gdocs= new GDocsManager(userToken, "userToken");
                out.print(gdocs.getDocumentTitleById(doc.getDocId()));
        		out.print("<form name=\"opendoc\" method=\"post\" action=\"\">");
        		out.print("<input type=\"submit\" name='opendocument' value=\"Open Document\"/>");
        		out.print("<input type=\"hidden\" name='doclink' value=\"" + gdocs.getDocumentLinkById(doc.getDocId()) + "\"/>");
        		out.print("</form>");
        		printGradingButton(out, launchRequest, request, num, doc.getUserId(), doc.getOrg());
        		num++;
                out.println("</li>");
        
            }
            out.println("</ol>");
            out.println("</p>");
        } catch (Exception e){
            AuthManager.printError(out, e);
        }   
	}
	
	
	private String gradeOperation(PrintWriter out,
			BLTILaunchRequest launchRequest, HttpServletRequest request) {
		
		// Get the user_id for the grade recipient
		String orgId = request.getParameter("orgId");
		String userId = request.getParameter("userId");
		userId = userId.substring(orgId.length());

		String setOutcomeResult = null;
		try {
			String roster = Lis.getRoster(out, request, launchRequest);
			String sourcedid = getUserLisPersonSourcedid(roster, userId);
			if (sourcedid == null) throw new Exception("Could not retrieve sourcedid for grade recipient.");
			
			setOutcomeResult = Lis.setSimpleOutcome(out, request, launchRequest, sourcedid);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return setOutcomeResult;
	}
	
	private String getUserLisPersonSourcedid(String roster, String userId) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(roster));
        try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.parse(is);
            doc.getDocumentElement().normalize();
            
            // Get communication success status
            NodeList statusList = doc.getElementsByTagName("statusinfo");
            Node status = statusList.item(0);
            Element statusElement = (Element)status;
            NodeList codeList = statusElement.getElementsByTagName("codemajor");
            Node code = codeList.item(0);
            Element codeElement = (Element)code;
            NodeList textCode = codeElement.getChildNodes();
            String success = ((Node)textCode.item(0)).getNodeValue().trim();
            
            if (!success.equals("Success")) {
            	throw new IOException("Error retrieving student roster!");
            }
            
            // Retrieve user_ids
            NodeList memberList = doc.getElementsByTagName("member");
            int totalMembers = memberList.getLength();
            for (int m = 0; m < totalMembers; m++) {
            	Node firstMemberNode = memberList.item(m);
            	
            	if (firstMemberNode.getNodeType() == Node.ELEMENT_NODE) {
            		Element firstMemberElement = (Element)firstMemberNode;
            		
            		//Get user_id
            		NodeList userIdList = firstMemberElement.getElementsByTagName("user_id");
                    Element userIdElement = (Element)userIdList.item(0);

                    NodeList userIdText = userIdElement.getChildNodes();
                    String memberId = ((Node)userIdText.item(0)).getNodeValue().trim();
                    
                    if (memberId.equals(userId)) {
                    	// Get lis_result_sourcedid
                		NodeList sourcedidList = firstMemberElement.getElementsByTagName("lis_result_sourcedid");
                        Element sourcedidElement = (Element)sourcedidList.item(0);

                        NodeList sourcedidText = sourcedidElement.getChildNodes();
                        String sourcedid = ((Node)sourcedidText.item(0)).getNodeValue().trim();
                        return sourcedid;
                    }
            	}
            }
            
            
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        return null;
	}
	
	private void printIframe(PrintWriter out,
			BLTILaunchRequest launchRequest, HttpServletRequest request) {
		
	        out.println("<p>");
			out.println("<iframe src=\"" + request.getParameter("doclink") +"\" width=\"100%\" height=\"100%\" ></iframe>");
			out.println("</p>");
	}

}
