package org.raviolini.aspects.io.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.raviolini.aspects.io.configuration.ConfigService;

public class LogFactory {

    private static String namespace = "raviolini.logging";
    
    public static Logger getLogger() {
        try {
            return loadFromConfig();
        } catch (IOException | SecurityException e) {
            return loadDefault();
        }
    }
    
    private static Logger loadFromConfig() throws SecurityException, IOException {
        ConfigService config = new ConfigService();
        LogManager.getLogManager().readConfiguration(config.getFile());
        Logger logger = Logger.getLogger(namespace); 
        
        return logger;
    }
    
    private static Logger loadDefault() {
        Logger logger = Logger.getLogger(namespace);
        logger.addHandler(new ConsoleHandler());
        
        return logger;
    }
}