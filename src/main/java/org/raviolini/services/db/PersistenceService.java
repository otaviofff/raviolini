package org.raviolini.services.db;

import java.io.IOException;
import java.util.Map;

import org.raviolini.services.io.ConfigurationService;
import org.raviolini.services.io.LoggingService;

public abstract class PersistenceService {
    
    private ConfigurationService config;
    private LoggingService log;
    private String namespace;
    private String[] keys;
    
    protected void setConfigNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    protected void setConfigKeys(String[] keys) {
        this.keys = keys;
    }
    
    private ConfigurationService getConfig() {
        if (config == null) {
            config = new ConfigurationService();
        }
        
        return config;
    }
    
    private LoggingService getLog() {
        if (log == null) {
            log = new LoggingService();
        }
        
        return log;
    }
    
    protected Map<String, String> getProperties() throws IOException {
        return getConfig().read(namespace, keys);
    }
    
    protected void logException(Exception e) {
        getLog().logException(e, true);
    }
}