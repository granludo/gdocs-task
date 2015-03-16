package edu.upc.essi.sushitos.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * ToolConfiguration Class
 * 
 * It is used to retrieve system configuration from a config file.
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class ToolConfiguration {
    private static ToolConfiguration configObject;
    private static Properties propertiesObject;

    /**
     * Private constructor for a Singleton object
     * 
     * @throws IOException
     */
    private ToolConfiguration() throws IOException {
        String valuesFile = "/" + ToolConfiguration.class.getPackage().getName().replace(".", "/") +
                            "/tool.properties";

        // Get the URL of the configuration file
        URL resourceUrl = ToolConfiguration.class.getClassLoader().getResource(valuesFile);
        if (resourceUrl == null) {
            throw new IOException("Configuration file not found: " + valuesFile);
        }

        // Open configuration file for reading
        InputStream input = resourceUrl.openStream();

        try {
            Properties p = new Properties();
            p.load(input);
            propertiesObject = p;
        } finally {
            input.close();
        }
    }

    /**
     * Function returning the object containing the configuration parameters
     * 
     * @return
     * @throws IOException
     */
    public static ToolConfiguration getToolConfiguration() throws IOException {
        if (configObject == null) {
            configObject = new ToolConfiguration();
        }
        return configObject;
    }

    public String getProperty(String component, String key) {
        String s = propertiesObject.getProperty(component + "." + key);
        return s;
    }
}