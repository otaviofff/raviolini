package org.raviolini.aspects.io.configuration;

import java.util.Map;

import org.raviolini.aspects.io.configuration.drivers.AbstractConfigDriver;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class ConfigService {

    private Boolean preferEnv;
    private AbstractConfigDriver driver;
    
    
    public ConfigService(Boolean preferEnv) {
        this.preferEnv = preferEnv;
    }
    
    public ConfigService() {
        this(false);
    }
    
    private AbstractConfigDriver getDriver() {
        if (driver == null) {
            driver = ConfigFactory.getDriver(preferEnv);
        }
        
        return driver;
    }
    
    public String read(String namespace, String key) throws UnloadableConfigException {
        return read(namespace, new String[] {key}).get(key);
    }
    
    public Map<String, String> read(String namespace, String[] keys) throws UnloadableConfigException  {
        return getDriver().read(namespace, keys);
    }
}