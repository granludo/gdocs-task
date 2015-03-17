package edu.upc.essi.sushitos.log;

import java.util.logging.*;
import java.io.*;

import edu.upc.essi.sushitos.configuration.ToolConfiguration;

/**
 * SystemLogger Class
 * It is used to log system actions.
 * Target out:
 *          Console for debbuging purposes 
 *          File for production     
 * 
 * @author ngalanis
 * @author jpiguillem
 * TODO: I don't know why it logs in several files....
 */
public class SystemLogger extends Logger{

    protected SystemLogger(String name, String resourceBundleName) throws SecurityException, IOException {

        super(name, resourceBundleName);
        boolean append = true;
        ToolConfiguration config = ToolConfiguration.getToolConfiguration();
        FileHandler fh = new FileHandler(config.getProperty("system", "path") + name+".log", append);
        //fh.setFormatter(new XMLFormatter());
        fh.setFormatter(new Formatter() {
            public String format(LogRecord rec) {
                StringBuffer buf = new StringBuffer(1000);
                buf.append(new java.util.Date());
                buf.append(' ');
                buf.append(rec.getLevel());
                buf.append(' ');
                buf.append(formatMessage(rec));
                buf.append('\n');
                return buf.toString();
                }
              });

        addHandler(fh);
        addHandler(new ConsoleHandler());

    }
    
    public static SystemLogger getSystemLogger(){
        try {
            return new SystemLogger("system", null);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
        
    }
    
}
