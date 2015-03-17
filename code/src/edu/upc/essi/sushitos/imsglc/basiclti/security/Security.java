package edu.upc.essi.sushitos.imsglc.basiclti.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthValidator;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;
import net.oauth.signature.OAuthSignatureMethod;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.Organization;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.OrganizationManager;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;

/**
 * Security class
 * 
 * It implements some method to sign requests
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class Security {

    // We use the built-in Java logger because this code needs to be very
    // generic
    private static Logger M_log = Logger.getLogger(Security.class.toString());

    public static final String BASICLTI_SUBMIT = "basiclti_submit";

    // Remove any properties which we will not send
    public static Properties cleanupProperties(Properties newMap) {

        Properties newProp = new Properties();
        for (Object objKey : newMap.keySet()) {
            if (!(objKey instanceof String))
                continue;

            String key = (String) objKey;
            if (key == null)
                continue;

            String value = newMap.getProperty(key);
            if (value == null)
                continue;
            if (key.startsWith("internal_"))
                continue;
            if (key.startsWith("_"))
                continue;
            if ("action".equalsIgnoreCase(key))
                continue;
            if ("launchurl".equalsIgnoreCase(key))
                continue;
            if (value.equals(""))
                continue;
            newProp.setProperty(key, value);
        }
        return newProp;
    }

    public static String signProperties(Properties postProp, String url, String method, String key,
            String secret, String org_secret, String org_id, String org_desc) {

        postProp = Security.cleanupProperties(postProp);
        if (postProp.getProperty("lti_version") == null) {
            postProp.setProperty("lti_version", "LTI-1p0");
        }
        if (postProp.getProperty("lti_message_type") == null) {
            postProp.setProperty("lti_message_type", "basic-lti-launch-request");
        }

        // Allow caller to internationalize this for us...
        if (postProp.getProperty(BASICLTI_SUBMIT) == null) {
            postProp.setProperty(BASICLTI_SUBMIT, "Launch Endpoint with BasicLTI Data");
        }
        if (org_id != null)
            postProp.setProperty("tool_consumer_instance_guid", org_id);
        if (org_desc != null)
            postProp.setProperty("tool_consumer_instance_description", org_desc);

        String oauth_consumer_key = key;
        String oauth_consumer_secret = secret;
        if (org_secret != null) {
            oauth_consumer_secret = org_secret;
            oauth_consumer_key = org_id;
        }

        if (postProp.getProperty("oauth_callback") == null)
            postProp.setProperty("oauth_callback", "about:blank");

        if (oauth_consumer_key == null || oauth_consumer_secret == null) {
            System.out.println("Error in signProperties - key and secret must be specified");
            return null;
        }

        OAuthMessage oam = new OAuthMessage(method, url, postProp.entrySet());
        OAuthConsumer cons = new OAuthConsumer("about:blank", oauth_consumer_key,
                oauth_consumer_secret, null);
        OAuthAccessor acc = new OAuthAccessor(cons);
        try {
            oam.addRequiredParameters(acc);
            System.out.println("Base Message String\n" + OAuthSignatureMethod.getBaseString(oam)
                    + "\n");

            List<Map.Entry<String, String>> params = oam.getParameters();

            String ret = "";
            for (Map.Entry<String, String> e : params) {
                ret += e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8") + "&";
            }
            return ret;

        } catch (net.oauth.OAuthException e) {
            M_log.warning("BasicLTIUtil.signProperties OAuth Exception " + e.getMessage());
            return null;
        } catch (java.io.IOException e) {
            M_log.warning("BasicLTIUtil.signProperties IO Exception " + e.getMessage());
            return null;
        } catch (java.net.URISyntaxException e) {
            M_log.warning("BasicLTIUtil.signProperties URI Syntax Exception " + e.getMessage());
            return null;
        }

    }
    
    public static void validateRequest(HttpServletRequest request) throws OAuthException, ClassNotFoundException, SQLException, NeedCreateException, IOException, UnexistantOrganizationException{

        OAuthMessage oam = OAuthServlet.getMessage(request, null);
        OAuthValidator oav = new SimpleOAuthValidator();
        String oauth_consumer_key = request.getParameter("oauth_consumer_key");
        if ( oauth_consumer_key == null ) {
            throw new OAuthException("OAUTH CONSUMER KEY is missing");
        }
        OAuthConsumer cons = null;
        
        OrganizationManager orgMan = new OrganizationManager();
        Organization org = orgMan.getOrganization(oauth_consumer_key);
        
        cons = new OAuthConsumer("http://call.back.url.com/", org.getOrgId(), org.getSecret(), null);

        OAuthAccessor acc = new OAuthAccessor(cons);

        try {
            System.out.println("\n<b>Base Message</b>\n</pre><p>\n");
            System.out.println(OAuthSignatureMethod.getBaseString(oam));
            System.out.println("<pre>\n");
            oav.validateMessage(oam,acc);
            System.out.println("Message validated");
        } catch(Exception e) {
            System.out.println("<b>Error while valdating message:</b>\n");
            System.out.println(e);
        }
    }
}
