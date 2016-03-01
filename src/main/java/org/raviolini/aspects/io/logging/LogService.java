package org.raviolini.aspects.io.logging;

import java.util.Map;
import java.util.logging.Logger;

import org.raviolini.aspects.io.configuration.ConfigService;

import airbrake.AirbrakeNotice;
import airbrake.AirbrakeNoticeBuilder;
import airbrake.AirbrakeNotifier;

public class LogService {

    private Logger logger;
    
    private ConfigService getConfig() {
        return new ConfigService();
    }
    
    private String[] getConfigKeys() {
        return new String[] {"AIRBRAKE_API_KEY", "AIRBRAKE_ENV"};
    }
    
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
        notifyException(e);
        
        if (logTrace) {
            getLogger().severe(LogEntry.composeStackTraceEntry(e));
        } else {
            getLogger().warning(LogEntry.composeExceptionEntry(e));
        }
    }
    
    private Integer notifyException(Exception e) {
        try {
            Map<String, String> keys = getConfig().read("", getConfigKeys());
            
            String apiKey = keys.get("AIRBRAKE_API_KEY");
            String envKey = keys.get("AIRBRAKE_ENV");
            
            if (apiKey == null || envKey == null || apiKey.isEmpty() || envKey.isEmpty()) {
                return -1;
            }
    
            AirbrakeNotice notice = new AirbrakeNoticeBuilder(apiKey, e, envKey).newNotice();
            AirbrakeNotifier notifier = new AirbrakeNotifier();
            
            return notifier.notify(notice);
        } catch (Exception ignored) {
            return -1;
        }
    }
}