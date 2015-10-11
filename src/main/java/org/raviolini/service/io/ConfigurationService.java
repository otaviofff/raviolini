package org.raviolini.service.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationService {

    private InputStream stream;
    
    public InputStream getStream() {
        if (stream == null) {
            stream = getClass().getClassLoader().getResourceAsStream("application.properties");
        }
        
        return stream;
    }
    
    public Map<String, String> read(String namespace, String[] keys) throws IOException  {
        if (!namespace.isEmpty() && namespace.charAt(namespace.length() - 1) != '.') {
            namespace += '.';
        }
        
        Map<String, String> map = new HashMap<>();
        Properties properties = new Properties();
        
        properties.load(getStream());
        
        for (String key : keys) {
            map.put(key, properties.getProperty(namespace + key));
        }
        
        getStream().close();
        
        return map;
    }
    
    public String read(String namespace, String key) throws IOException {
        return read(namespace, new String[] {key}).get(key);
    }
}