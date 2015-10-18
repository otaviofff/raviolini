package org.raviolini.aspects.data.database;

import java.io.IOException;
import java.util.Map;

import org.raviolini.aspects.data.database.drivers.AbstractDatabaseDriver;
import org.raviolini.aspects.data.database.drivers.RelationalDatabaseDriver;
import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.domain.Entity;

public class DatabaseFactory {

    private static String getConfigNamespace() {
        return "raviolini.database";
    }
    
    private static String[] getConfigKeys() {
        return new String[] {"driver", "engine", "host", "port", "name", "user", "pass"};
    }
    
    public static <T extends Entity> AbstractDatabaseDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        String driver, engine, host, name, user, pass;
        Integer port;
        
        ConfigService config = new ConfigService();
     
        try {
            Map<String, String> map = config.read(getConfigNamespace(), getConfigKeys());
            
            driver = map.get("driver");
            engine = map.get("engine");
            host = map.get("host");
            port = Integer.valueOf(map.getOrDefault("port", "0"));
            name = map.get("name");
            user = map.get("user");
            pass = map.get("pass");
        } catch (IOException e) {
            throw new UnloadableConfigException();
        } catch (NumberFormatException e) {
            throw new InvalidPropertyException();
        }
        
        return instantiateDriver(driver, engine, host, port, name, user, pass);
    }
    
    private static <T extends Entity> AbstractDatabaseDriver<T> instantiateDriver(String driver, String engine, String host, Integer port, String name, String user, String pass) throws InvalidPropertyException {
        switch(driver) {
            case "relational":
                return new RelationalDatabaseDriver<>(engine, host, port, name, user, pass);
            default:
                throw new InvalidPropertyException();
        }
    }
}
