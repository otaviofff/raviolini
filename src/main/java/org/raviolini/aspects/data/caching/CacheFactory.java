package org.raviolini.aspects.data.caching;

import java.io.IOException;
import java.util.Map;

import org.raviolini.aspects.data.caching.drivers.AbstractCacheDriver;
import org.raviolini.aspects.data.caching.drivers.NullCacheDriver;
import org.raviolini.aspects.data.caching.drivers.RedisCacheDriver;
import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.domain.Entity;

public class CacheFactory {

    private static String getConfigNamespace() {
        return "raviolini.cache";
    }
    
    private static String[] getConfigKeys() {
        return new String[] {"driver", "host", "port"};
    }
    
    public static <T extends Entity> AbstractCacheDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        String name, host;
        Integer port;
        
        ConfigService config = new ConfigService();
        
        try {
            Map<String, String> properties = config.read(getConfigNamespace(), getConfigKeys());
            
            name = properties.get("driver");
            host = properties.get("host");
            port = Integer.valueOf(properties.getOrDefault("port", "0"));
        } catch (IOException e ) {
            throw new UnloadableConfigException();
        } catch (NumberFormatException e) {
            throw new InvalidPropertyException();
        }
        
        return instantiateDriver(name, host, port);
    }
    
    private static <T extends Entity> AbstractCacheDriver<T> instantiateDriver(String name, String host, Integer port) throws InvalidPropertyException {
        switch (name) {
            case "redis":
                return new RedisCacheDriver<T>(host, port);
            case "null":
                return new NullCacheDriver<T>(host, port);
            default:
                throw new InvalidPropertyException();
        }
    }
}