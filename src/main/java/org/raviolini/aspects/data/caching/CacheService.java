package org.raviolini.aspects.data.caching;

import org.raviolini.aspects.data.caching.drivers.AbstractCacheDriver;
import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public class CacheService<T extends Entity> {

    private AbstractCacheDriver<T> driver;
    
    private AbstractCacheDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        if (driver == null) {
            driver = CacheFactory.<T>getDriver();
        }
        
        return driver;
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException, UnserializationException {
        return getDriver().get(entityId, entityClass);
    }
    
    public Boolean set(T entity, Class<T> entityClass) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException, SerializationException {
        return getDriver().set(entity, entityClass);
    }
    
    public Boolean delete(Integer entityId) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().delete(entityId);
    }
    
    public Boolean exists(Integer entityId) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().exists(entityId);
    }
}