package edu.upc.essi.sushitos.imsglc.basiclti.extensions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;

import org.xml.sax.SAXException;

import edu.upc.essi.sushitos.imsglc.basiclti.extensions.serviceresponses.ReadMembershipsServiceResponse;
import edu.upc.essi.sushitos.imsglc.basiclti.httpclient.Connection;
import edu.upc.essi.sushitos.imsglc.basiclti.httpserver.BLTILaunchRequest;
import edu.upc.essi.sushitos.imsglc.basiclti.security.Security;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.Organization;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;

public class MembershipsService {
    // readMembershipsForContext
    public static ReadMembershipsServiceResponse readMemberships(
            BLTILaunchRequest launchRequest)
            throws ParserConfigurationException, SAXException, IOException, UnexistantOrganizationException, Exception {
        String message = "basic-lis-readmembershipsforcontext";

        String url = launchRequest.getExtImsLisMembershipsUrl();

        Properties data = new Properties();
        data.setProperty("lti_message_type", message);
        data.setProperty("id", launchRequest.getExtImsLisMembershipsId());

        String oauth_consumer_key = launchRequest.getOauthConsumerKey();
        OrganizationManager orgMan = new OrganizationManager();
        Organization org;
        try {
            org = orgMan.getOrganization(oauth_consumer_key);
        } catch (Exception e) {
            throw new Exception("internal error");
        }
        String oauth_consumer_secret = org.getSecret();

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
        return new ReadMembershipsServiceResponse(postResponse);
    }
}
