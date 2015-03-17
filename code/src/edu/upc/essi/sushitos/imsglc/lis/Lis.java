package edu.upc.essi.sushitos.imsglc.lis;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.upc.essi.sushitos.imsglc.basiclti.httpclient.Connection;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.basiclti.security.Security;

/**
 * Lis Class This class implements some method to support IMS LIS
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class Lis {

    // TODO: Implement
    public static String getProfessorMail() {
        return "prof@gmail.com";
    }

    // TODO: Implement
    public static int[] getGroups(int courseID) {
        int[] returns = {0, 1, 2};
        return returns;
    }

    // TODO: Implement
    public static int[] getGroupMembers(int groupID) {
        int[] returns = {0, 1};
        return returns;
    }

    // TODO: Implement
    public static int[] getCourseStudents(int courseID) {
        int[] returns = {0, 1, 2, 4};
        return returns;
    }

    public static String setSimpleOutcome(HttpServletRequest request,
            BLTILaunchRequest launchRequest, String sourcedid)
            throws MalformedURLException {

        String message = "";
        if (request.getParameter("submitgrade") != null) {
            if (request.getParameter("submitgrade").equals("SendGrade")
                    && (request.getParameter("grade") != null)) {
                message = "basic-lis-replaceresult";
            } else if (request.getParameter("submitgrade").equals("ReadGrade")) {
                message = "basic-lis-readresult";
            } else if (request.getParameter("submitgrade")
                    .equals("DeleteGrade")) {
                message = "basic-lis-deleteresult";
            } else {
                return "Error: Unidentified request parameter!";
            }
        }

        String url = launchRequest.getExtImsLisBasicOutcomeUrl();

        Properties data = new Properties();
        data.setProperty("lti_message_type", message);
        data.setProperty("sourcedid", sourcedid);
        data.setProperty("result_statusofresult", "final");
        data.setProperty("result_resultvaluesourcedid", "decimal");
        data.setProperty("result_resultscore_textstring",
                request.getParameter("grade"));

        String oauth_consumer_key = launchRequest.getOauthConsumerKey();
        // TODO: Should get key from database.
        String oauth_consumer_secret = "secret";

        // Properties newData = new Properties();
        String newData = Security.signProperties(data, url, "POST",
                oauth_consumer_key, oauth_consumer_secret, null,
                launchRequest.getToolConsumerInstanceGuid(),
                launchRequest.getToolConsumerInstanceDescription());

        String postResponse = "";
        try {
            Connection connection = new Connection(url);
            connection.doPost(newData);
            postResponse = connection.getResponse();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String returns = parseResponse(message, postResponse);
        return returns;
    }

    private static String parseResponse(String message, String response) {
        String returns = "";
        XPath xpath = XPathFactory.newInstance().newXPath();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder;
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(response));
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.parse(is);
            doc.getDocumentElement().normalize();
            System.out.println("yayayayayaya");
            System.out.println(doc);
            // Get communication success status
            // NodeList statusList = doc.getElementsByTagName("statusinfo");
            // Node status = statusList.item(0);
            // Element statusElement = (Element) status;
            // NodeList codeList =
            // statusElement.getElementsByTagName("codemajor");
            // Node code = codeList.item(0);
            // Element codeElement = (Element) code;
            // NodeList textCode = codeElement.getChildNodes();
            // String success = ((Node) textCode.item(0)).getNodeValue().trim();
            //
            // if (!success.equals("Success")) {
            // throw new IOException("Error retrieving student roster!");
            // }
            // if (message.equals("basic-lis-replaceresult")) {
            // returns = "Grade successfully committed";
            // } else if (message.equals("basic-lis-deleteresult")) {
            // returns = "Grade successfully deleted";
            // } else if (message.equals("basic-lis-readresult")) {
            // NodeList resultList = doc.getElementsByTagName("result");
            // Node result = resultList.item(0);
            // Element resultElement = (Element) result;
            // NodeList resultScoreList = resultElement
            // .getElementsByTagName("resultscore");
            // Node resultScore = resultScoreList.item(0);
            // Element resultScoreElement = (Element) resultScore;
            // NodeList resultTextList = resultScoreElement
            // .getElementsByTagName("textstring");
            // Node resultText = resultTextList.item(0);
            // Element resultTextElement = (Element) resultText;
            // NodeList note = resultTextElement.getChildNodes();
            // returns = "Current grade is: "
            // + ((Node) note.item(0)).getNodeValue().trim();
            // }

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

        return returns;
    }
}
