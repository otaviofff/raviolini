package org.raviolini.service.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingService {

    private Logger logger;
    private Handler handler;
    
    protected Map<String, String> getProperties() throws IOException {
        String namespace = "raviolini.log";
        String[] keys = new String[] {"limit", "count", "append"};
        
        return new ConfigurationService().read(namespace, keys);
    }
    
    private String getFileName() {
        String date = new SimpleDateFormat("yyyyMM").format(new Date());
        String name = ("log/raviolini_").concat(date).concat(".log");
        
        return name;
    }
    
    private Handler getHandler() {
        if (handler == null) {
            try {
                Map<String, String> properties = getProperties();
                
                handler = new FileHandler(getFileName(), 
                        Integer.valueOf(properties.get("limit")),
                        Integer.valueOf(properties.get("count")), 
                        Boolean.valueOf(properties.get("append"))
                        );
            } catch (SecurityException | IOException e) {
                handler = new ConsoleHandler();
            } finally {
                handler.setFormatter(new SimpleFormatter());
            }
        }
        
        return handler;
    }
    
    private Logger getLogger() {
        if (logger == null) {
            LogManager.getLogManager().reset();
            logger = Logger.getLogger("org.raviolini");
            logger.addHandler(getHandler());
            logger.setLevel(Level.ALL);
        }
        
        return logger;
    }
    
    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        e.printStackTrace(pw);
        
        return sw.toString();
    }

    public void logMessage(String message) {
        getLogger().info(message);
        getHandler().close();
    }
    
    public void logException(Exception e, Boolean logTrace) {
        if (logTrace) {
            getLogger().severe(getStackTrace(e));
        } else {
            getLogger().warning(e.getMessage() + "\n");
        }
        
        getHandler().close();
    }
}