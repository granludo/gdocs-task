package edu.upc.essi.sushitos.imsglc.basiclti.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * CommManager
 * 
 * It is used to manage callbacks to LMSs
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 *
 */
public class CommManager {

	public static String postData(String endPoint, String message) throws IOException {
		URL url = new URL(endPoint);
	    BufferedReader reader = null;
	    StringBuilder stringBuilder;

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	      
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setReadTimeout(15*1000);
		connection.connect();

		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    stringBuilder = new StringBuilder();

	    String line = null;
	    while ((line = reader.readLine()) != null)
	    {
	    	stringBuilder.append(line + "\n");
	    }
	    reader.close();
	    return stringBuilder.toString();
	}
	
	public static String buildPostForm(Properties data, String endPoint, String formName) {
		String postForm = "";
		postForm += "<div id=\"ltiFormSubmitArea\">\n";
		postForm += "<form action=\"" + endPoint + "\" name=\"" + formName + "\" id=\"" + formName + "\" method=\"post\" encType=\"application/x-www-form-urlencoded\">\n";

		for (Object objKey : data.keySet()) {
            if (!(objKey instanceof String)) continue;
            
            String key = (String) objKey;
            if (key == null) continue;
            
            String value = data.getProperty(key);
            if ( value == null ) continue;
            
            // This will escape the contents pretty much - at least 
            // we will be safe and not generate dangerous HTML
            key = htmlspecialchars(key);
            value = htmlspecialchars(value);
            if (key.equals("lti_message_type")) {
            	postForm += "<input type=\"submit\" name=\"";
            } else { 
            	postForm += "<input type=\"hidden\" name=\"";
            }
            postForm += key;
            postForm += "\" value=\"";
            postForm += value;
            postForm += "\"/>\n";
		}
		postForm += "</form>\n";
		postForm += "</div>\n";
		
		return postForm;
	}
	
	// Basic utility to encode form text - handle the "safe cases"
	public static String htmlspecialchars(String input)
	{
	    if ( input == null ) return null;
	    String retval = input.replace("&", "&amp;");
	    retval = retval.replace("\"", "&quot;");
	    retval = retval.replace("<", "&lt;");
	    retval = retval.replace(">", "&gt;");
	    retval = retval.replace(">", "&gt;");
	    retval = retval.replace("=", "&#61;");
	    return retval;
	}
}
