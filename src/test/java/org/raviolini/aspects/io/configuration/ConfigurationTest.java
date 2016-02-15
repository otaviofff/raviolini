package org.raviolini.aspects.io.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class ConfigurationTest {
    
    @Test
    public void testFileConfiguration() throws UnloadableConfigException {
        ConfigService config = new ConfigService();
        
        String value1 = config.read("raviolini.database", "driver");
        String value2 = config.read("raviolini.database", "invalid");
        
        assertEquals("relational", value1);
        assertNull(value2);
    }
    
    @Test
    public void testFileConfigurationMap() throws UnloadableConfigException {
        String[] keys = new String[] {"driver", "engine", "host", "port"};
        Map<String, String> values = new ConfigService().read("raviolini.database", keys);
        
        assertEquals("relational", values.get("driver"));
        assertEquals("postgresql", values.get("engine"));
        assertEquals("localhost", values.get("host"));
        assertEquals("15432", values.get("port"));
        assertNull(values.get("name"));
    }
    
    @Test
    public void testEnvironmentConfigurationMap() throws UnloadableConfigException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Map<String, String> newEnv = new HashMap<>();
        newEnv.put("raviolini.database.driver", "relational");
        newEnv.put("raviolini.database.engine", "mysql");
        
        EnvironmentMock mock = new EnvironmentMock();
        mock.setEnv(newEnv);
        
        String[] keys = new String[] {"driver", "engine"};
        Map<String, String> values = new ConfigService(true).read("raviolini.database", keys);
        
        assertEquals("relational", values.get("driver"));
        assertEquals("mysql", values.get("engine"));
        assertNull(values.get("name"));
        
        mock.resetEnv();
    }
}