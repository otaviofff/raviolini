package org.raviolini.aspects.io.logging;

import java.util.logging.Logger;

public class LogService {

    private Logger logger;
    
    private Logger getLogger() {
        if (logger == null) {
            logger = LogFactory.getLogger();
        }
        
        return logger;
    }
    
    public void logMessage(String message) {
        getLogger().info(message);
    }
    
    public void logException(Exception e, Boolean logTrace) {
        if (logTrace) {
            getLogger().severe(LogEntry.composeStackTraceEntry(e));
        } else {
            getLogger().warning(LogEntry.composeExceptionEntry(e));
        }
    }
}