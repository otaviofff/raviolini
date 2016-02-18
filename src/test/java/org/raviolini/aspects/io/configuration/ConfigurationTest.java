package org.raviolini.aspects.io.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.raviolini.aspects.io.configuration.drivers.EnvConfigDriver;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class ConfigurationTest {
    
    @Test
    public void testFileConfiguration() throws UnloadableConfigException {
        ConfigService config = new ConfigService();
        
        assertEquals("relational", config.read("raviolini.database", "driver"));
        assertNull(config.read("raviolini.database", "invalid"));
    }
    
    @Test
    public void testFileConfigurationMap() throws UnloadableConfigException {
        Map<String, String> map = new ConfigService().read("raviolini.database", new String[] {"driver", "engine", "host", "port"});
        
        assertEquals("relational", map.get("driver"));
        assertEquals("postgresql", map.get("engine"));
        assertEquals("localhost", map.get("host"));
        assertEquals("15432", map.get("port"));
        assertNull(map.get("name"));
    }
    
    @Test
    public void testEnvironmentConfigurationMap() throws UnloadableConfigException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ConfigRegistry.getInstance().set(new EnvConfigDriver());
        
        Map<String, String> newEnv = new HashMap<>();
        newEnv.put("raviolini.database.driver", "relational");
        newEnv.put("raviolini.database.engine", "mysql");
        
        EnvironmentMock mock = new EnvironmentMock();
        mock.set(newEnv);
        
        Map<String, String> map = new ConfigService().read("raviolini.database", new String[] {"driver", "engine"});
        
        assertEquals("relational", map.get("driver"));
        assertEquals("mysql", map.get("engine"));
        assertNull(map.get("name"));
        
        mock.reset();
        ConfigRegistry.getInstance().reset();
    }
}