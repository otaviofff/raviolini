package org.raviolini.aspects.io.configuration;

import org.raviolini.aspects.io.configuration.drivers.AbstractConfigDriver;

public class ConfigRegistry {
    
    private static ConfigRegistry registry;
    private AbstractConfigDriver driver;
    
    private ConfigRegistry() {
        // Singleton constructor. Leave it private and empty.
    }
    
    public void set(AbstractConfigDriver driver) {
        this.driver = driver;
    }
    
    public AbstractConfigDriver get() {
        return this.driver;
    }
    
    public void reset() {
        this.driver = null;
    }
    
    public Boolean loaded() {
        return this.driver != null;
    }
    
    public static ConfigRegistry getInstance() {
        if (registry == null) {
            registry = new ConfigRegistry();
        }
        
        return registry;
    }
}