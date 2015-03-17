package edu.upc.essi.sushitos.imsglc.basiclti.extensions;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.DeleteResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.ReadResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.UpdateResultServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.httpclient.Connection;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.basiclti.security.Security;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.Organization;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;

public class OutcomesService {
    private static String outcomesAction(String grade,
            BLTILaunchRequest launchRequest, String sourcedid, String message) throws Exception{
        String url = launchRequest.getExtImsLisBasicOutcomeUrl();

        Properties data = new Properties();
        data.setProperty("lti_message_type", message);
        data.setProperty("sourcedid", sourcedid);
        data.setProperty("result_statusofresult", "final");
        data.setProperty("result_resultvaluesourcedid", "decimal");
        data.setProperty("result_resultscore_textstring", grade);

        String oauth_consumer_key = launchRequest.getOauthConsumerKey();
        OrganizationManager orgMan = new OrganizationManager();
        Organization org;
        try {
            org = orgMan.getOrganization(oauth_consumer_key);
        } catch (Exception e) {
            throw new Exception("internal error");
        }
        String oauth_consumer_secret = org.getSecret();

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
        System.out.println(postResponse);
        System.out.println(sourcedid);
        return postResponse;
    }

    // updateResult
    public static UpdateResultServiceResponse updateResult(String grade,
            BLTILaunchRequest launchRequest, String sourcedid)
            throws ParserConfigurationException, SAXException, IOException, Exception {
        String message = "basic-lis-updateresult";

        String response = outcomesAction(grade, launchRequest, sourcedid,
                message);
        return new UpdateResultServiceResponse(response);
    }

    // readResult
    public static ReadResultServiceResponse readResult(
            BLTILaunchRequest launchRequest, String sourcedid)
            throws ParserConfigurationException, SAXException, IOException, Exception {
        String message = "basic-lis-readresult";

        String response = outcomesAction("", launchRequest, sourcedid, message);
        return new ReadResultServiceResponse(response);
    }

    // deleteResult
    public static DeleteResultServiceResponse deleteResult(
            BLTILaunchRequest launchRequest, String sourcedid)
            throws ParserConfigurationException, SAXException, IOException, Exception {
        String message = "basic-lis-deleteresult";

        String response = outcomesAction("", launchRequest, sourcedid, message);
        return new DeleteResultServiceResponse(response);
    }
}
