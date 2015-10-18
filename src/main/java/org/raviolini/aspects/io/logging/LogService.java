package org.raviolini.aspects.io.logging;

import java.util.logging.Handler;
import java.util.logging.Logger;

public class LogService {

    private Logger logger;
    
    private Logger getLogger() {
        if (logger == null) {
            logger = LogFactory.getLogger();
        }
        
        return logger;
    }
    
    private void close() {
        if (logger == null) {
            return;
        }
        
        Handler[] handlers = logger.getHandlers();
        
        for (Handler handler : handlers) {
            handler.close();
        }
    }

    public void logMessage(String message) {
        getLogger().info(message);
        close();
    }
    
    public void logException(Exception e, Boolean logTrace) {
        if (logTrace) {
            getLogger().severe(LogEntry.composeStackTraceEntry(e));
        } else {
            getLogger().warning(LogEntry.composeExceptionEntry(e));
        }
        
        close();
    }
}