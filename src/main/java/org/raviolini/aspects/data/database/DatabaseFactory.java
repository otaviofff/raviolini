package org.raviolini.aspects.data.database;

import java.util.Map;

import org.raviolini.aspects.data.database.drivers.AbstractDatabaseDriver;
import org.raviolini.aspects.data.database.drivers.RelationalDatabaseDriver;
import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.domain.Entity;

public class DatabaseFactory {

    private static ConfigService getConfig() {
        return new ConfigService();
    }
    
    private static String getConfigNamespace() {
        return "raviolini.database";
    }
    
    private static String[] getConfigKeys() {
        return new String[] {"driver", "engine", "host", "port", "name", "user", "pass", "boot"};
    }
    
    public static <T extends Entity> AbstractDatabaseDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        Map<String, String> map = getConfig().read(getConfigNamespace(), getConfigKeys());
        
        String driver = map.get("driver");
        String engine = map.get("engine");
        String host = map.get("host");
        String name = map.get("name");
        String user = map.get("user");
        String pass = map.get("pass");
        Boolean boot = Boolean.valueOf(map.get("boot"));
        Integer port;
        
        try {
            port = Integer.valueOf(map.get("port"));    
        } catch (NumberFormatException e) {
            throw new InvalidPropertyException(getConfigNamespace().concat(".port"), e);
        }
        
        return instantiateDriver(driver, engine, host, port, name, user, pass, boot);
    }
    
    private static <T extends Entity> AbstractDatabaseDriver<T> instantiateDriver(String driver, String engine, String host, Integer port, String name, String user, String pass, Boolean boot) throws InvalidPropertyException {
        switch(driver) {
            case "relational":
                return new RelationalDatabaseDriver<T>(engine, host, port, name, user, pass, boot);
            default:
                throw new InvalidPropertyException(getConfigNamespace().concat(".driver"), null);
        }
    }
}