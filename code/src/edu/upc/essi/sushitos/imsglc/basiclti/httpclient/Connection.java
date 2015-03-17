package edu.upc.essi.sushitos.imsglc.basiclti.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Connection class
 * 
 * @author ngalanis
 * @author jpiguillem
 */
public class Connection {
	
	private URL server;
	private HttpURLConnection connection = null;

	public Connection(String url) throws MalformedURLException{
		server = new URL(url);
	}
	
	public void doPost(String data) throws IOException{
		connection = (HttpURLConnection) server.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
		
        wr.write(data);
        wr.flush();
        wr.close();
        
	}
	
	public String getResponse() throws IOException{
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        String result = "";
        while ((line = rd.readLine()) != null) {
        	result += line + '\n';
        }
        rd.close();
        
        return result;
	}
	
}
