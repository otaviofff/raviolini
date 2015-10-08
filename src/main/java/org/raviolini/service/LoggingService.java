package org.raviolini.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingService {

    private Logger logger;
    private Handler handler;

    //TODO: Set logging directory.
    //TODO: Create logging configuration file.
    
    private String getFileName() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String name = ("raviolini_").concat(date).concat(".log");
        
        return name;
    }
    
    private Handler getHandler() throws SecurityException, IOException {
        if (handler == null) {
            handler = new FileHandler(getFileName(), 524288000, 1, true);
            handler.setFormatter(new SimpleFormatter());
        }
        
        return handler;
    }
    
    private Logger getLogger() throws SecurityException, IOException {
        if (logger == null) {
            logger = Logger.getLogger("org.raviolini");
            logger.addHandler(getHandler());
            logger.setLevel(Level.SEVERE);
        }
        
        return logger;
    }
    
    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        e.printStackTrace(pw);
        
        return sw.toString();
    }

    public void logMessage(String message) throws SecurityException, IOException {
        getLogger().info(message);
        getHandler().close();
    }
    
    public void logException(Exception e, Boolean logTrace) throws SecurityException, IOException {
        if (logTrace) {
            getLogger().severe(getStackTrace(e));
        } else {
            getLogger().warning(e.getMessage());
        }
        
        getHandler().close();
    }
}