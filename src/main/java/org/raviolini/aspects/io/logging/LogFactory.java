package org.raviolini.aspects.io.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogFactory {

    private static InputStream getConfigFile() {
        return LogFactory.class.getClassLoader().getResourceAsStream("logging.properties");
    }
    
    private static String getConfigNamespace() {
        return "raviolini.logging";
    }
    
    public static Logger getLogger() {
        try {
            return loadConfigured();
        } catch (IOException | NullPointerException | SecurityException e) {
            //Graceful degradation.
            return loadDefault();
        }
    }
    
    private static Logger loadConfigured() throws IOException, NullPointerException, SecurityException {
        LogManager.getLogManager().readConfiguration(getConfigFile());
        Logger logger = Logger.getLogger(getConfigNamespace()); 
        
        return logger;
    }
    
    private static Logger loadDefault() {
        Logger logger = Logger.getLogger(getConfigNamespace());
        logger.addHandler(new ConsoleHandler());
        
        return logger;
    }
}