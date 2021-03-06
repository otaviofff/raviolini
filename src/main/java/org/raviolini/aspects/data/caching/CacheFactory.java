package org.raviolini.aspects.data.caching;

import java.util.Map;

import org.raviolini.aspects.data.caching.drivers.AbstractCacheDriver;
import org.raviolini.aspects.data.caching.drivers.MemcachedCacheDriver;
import org.raviolini.aspects.data.caching.drivers.NullCacheDriver;
import org.raviolini.aspects.data.caching.drivers.RedisCacheDriver;
import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.domain.Entity;

public class CacheFactory {

    private static ConfigService getConfig() {
        return new ConfigService();
    }
    
    private static String getConfigNamespace() {
        return "raviolini.cache";
    }
    
    private static String[] getConfigKeys() {
        return new String[] {"driver", "host", "port", "pass"};
    }
    
    public static <T extends Entity> AbstractCacheDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        Map<String, String> map = getConfig().read(getConfigNamespace(), getConfigKeys());
        
        String driver = map.get("driver");
        String host = map.get("host");
        String pass = map.get("pass");
        Integer port;
        
        try {
            port = (map.get("port") == null ? null : Integer.valueOf(map.get("port")));
        } catch (NumberFormatException e) {
            throw new InvalidPropertyException(getConfigNamespace().concat(".port"), e);
        }
        
        return instantiateDriver(driver, host, port, pass);
    }
    
    private static <T extends Entity> AbstractCacheDriver<T> instantiateDriver(String driver, String host, Integer port, String pass) throws InvalidPropertyException {
        if (driver == null) {
            driver = "invalid";
        }
        
        switch (driver.toLowerCase()) {
            case "redis":
                return new RedisCacheDriver<T>(host, port, pass);
            case "memcached":
                return new MemcachedCacheDriver<T>(host, port, pass);
            case "null":
                return new NullCacheDriver<T>(host, port, pass);
            default:
                throw new InvalidPropertyException(getConfigNamespace().concat(".driver"), null);
        }
    }
}