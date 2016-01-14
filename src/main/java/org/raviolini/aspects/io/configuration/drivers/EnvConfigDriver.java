package org.raviolini.aspects.io.configuration.drivers;

import java.util.HashMap;
import java.util.Map;

import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class EnvConfigDriver extends AbstractConfigDriver {

    private String readVariable(String key) throws UnloadableConfigException {
        try {
            return System.getenv(key);
        } catch (NullPointerException | SecurityException e) {
            throw new UnloadableConfigException(e);
        }
    }
    
    @Override
    protected Map<String, String> loadMap(String namespace, String[] keys) throws UnloadableConfigException {
        Map<String, String> map = new HashMap<>();
        
        for (String key : keys) {
            map.put(key, readVariable(namespace + key));
        }
        
        return map;
    }

    @Override
    public Boolean isActive() {
        return !System.getenv().isEmpty();
    }
}