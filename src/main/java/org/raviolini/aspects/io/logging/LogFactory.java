package org.raviolini.aspects.io.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.raviolini.aspects.io.configuration.ConfigService;

public class LogFactory {

    private static ConfigService getConfig() {
        return new ConfigService();
    }
    
    private static String getConfigNamespace() {
        return "raviolini.logging";
    }
    
    public static Logger getLogger() {
        try {
            return loadFromConfig();
        } catch (IOException | NullPointerException | SecurityException e) {
            //Graceful degradation.
            return loadDefault();
        }
    }
    
    private static Logger loadFromConfig() throws IOException, NullPointerException, SecurityException {
        LogManager.getLogManager().readConfiguration(getConfig().getFile());
        Logger logger = Logger.getLogger(getConfigNamespace()); 
        
        return logger;
    }
    
    private static Logger loadDefault() {
        Logger logger = Logger.getLogger(getConfigNamespace());
        logger.addHandler(new ConsoleHandler());
        
        return logger;
    }
}