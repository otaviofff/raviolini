package org.raviolini.aspects.io.configuration.drivers;

import java.util.Map;

import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public abstract class AbstractConfigDriver {
    
    public Map<String, String> read(String namespace, String[] keys) throws UnloadableConfigException  {
        namespace = checkConfigNamespace(namespace);
        
        return loadMap(namespace, keys);
    }
    
    private String checkConfigNamespace(String namespace) {
        if (namespace != null && 
            namespace.isEmpty() == false && 
            namespace.charAt(namespace.length() - 1) != '.') {
            namespace += '.';
        }
        
        return namespace;
    }
    
    protected abstract Map<String, String> loadMap(String namespace, String[] keys) throws UnloadableConfigException;
    
    public abstract Boolean isActive();
}