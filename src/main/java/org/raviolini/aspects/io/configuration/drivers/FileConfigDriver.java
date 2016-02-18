package org.raviolini.aspects.io.configuration.drivers;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class FileConfigDriver extends AbstractConfigDriver {

    private String fileName;
    private InputStream file;
    private Properties properties;
    
    public FileConfigDriver() {
        this.fileName = "application.properties";
    }
    
    public FileConfigDriver(String fileName) {
        this.fileName = fileName;
    }
    
    private InputStream getFile() {
        if (file == null) {
            file = getClass().getClassLoader().getResourceAsStream(fileName);
        }
        
        return file;
    }
    
    private Properties getProperties() throws IOException, NullPointerException {
        if (properties == null) {
            properties = new Properties();
            properties.load(getFile());
            getFile().close();
        }
        
        return properties;
    }
    
    private Map<String, String> loadMap(String namespace, String[] keys, Properties properties) {
        Map<String, String> map = new HashMap<>();
        
        for (String key : keys) {
            map.put(key, properties.getProperty(namespace + key));
        }
        
        return map;
    }
    
    @Override
    protected Map<String, String> loadMap(String namespace, String[] keys) throws UnloadableConfigException {
        try {
            return loadMap(namespace, keys, getProperties());
        } catch (NullPointerException | IOException e) {
            throw new UnloadableConfigException(e);
        }
    }

    @Override
    public Boolean isActive() {
        return getFile() != null;
    }
}