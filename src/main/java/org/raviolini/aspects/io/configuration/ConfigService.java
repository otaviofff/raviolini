package org.raviolini.aspects.io.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class ConfigService {

    private InputStream file;
    private Properties properties;
    
    public InputStream getFile() {
        if (file == null) {
            file = getClass().getClassLoader().getResourceAsStream("application.properties");
        }
        
        return file;
    }
    
    private Properties getProperties() throws IOException {
        if (properties == null) {
            properties = new Properties();
            properties.load(getFile());
            getFile().close();
        }
        
        return properties;
    }
    
    private String normalizeNamespace(String namespace) {
        if (!namespace.isEmpty() && namespace.charAt(namespace.length() - 1) != '.') {
            namespace += '.';
        }
        
        return namespace;
    }
    
    private Map<String, String> loadMap(String namespace, String[] keys) throws IOException {
        Properties properties = getProperties();
        
        Map<String, String> map = new HashMap<>();
        
        for (String key : keys) {
            map.put(key, properties.getProperty(namespace + key));
        }
        
        return map;
    }
    
    public Map<String, String> read(String namespace, String[] keys) throws UnloadableConfigException  {
        namespace = normalizeNamespace(namespace);
        
        try {
            return loadMap(namespace, keys);
        } catch (IOException e) {
            throw new UnloadableConfigException();
        }
    }
    
    public String read(String namespace, String key) throws UnloadableConfigException {
        return read(namespace, new String[] {key}).get(key);
    }
}