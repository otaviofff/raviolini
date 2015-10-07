package org.raviolini.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

public class LoggingService {

    private Logger logger;

    private Handler getHandler() throws SecurityException, IOException {
        //TODO: Set looging directory.
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String name = ("raviolini_").concat(date).concat(".log");
        Handler handler = new FileHandler(name, 524288000, 1, true);
        Formatter formatter = new XMLFormatter();
        
        handler.setFormatter(formatter);
        
        return handler;
    }
    
    public LoggingService() {
        try {
            //TODO: Create logging configuration file.
            //TODO: Check why different log files are being created.
            logger = Logger.getLogger("org.raviolini");
            logger.addHandler(getHandler());
            logger.setLevel(Level.SEVERE);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void logMessage(String message) {
        if (logger == null) return;
        
        logger.info(message);
    }
    
    public void logException(Exception e, Boolean logTrace) {
        if (logger == null) return;
        
        if (logTrace) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.severe(sw.toString());
        } else {
            logger.warning(e.getMessage());
        }
    }
    
    public void logException(Exception e) {
        logException(e, true);
    }
}