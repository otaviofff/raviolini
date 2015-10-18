package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public abstract class CacheDriver<T extends Entity> {

    private String host;
    private Integer port;
    private SerializationService<T> serializer;
    
    public CacheDriver(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.serializer = null;
    }
    
    public String getHost() {
        return host;
    }
    
    public Integer getPort() {
        return port;
    }
    
    protected SerializationService<T> getSerializer() {
        if (serializer == null) {
            serializer = new SerializationService<>();
        }
        
        return serializer;
    }
    
    public abstract T get(Integer entityId, Class<T> entityClass) throws CacheConnectionException, UnserializationException, UnloadableConfigException, InvalidPropertyException;
    
    public abstract Boolean set(T entity, Class<T> entityClass) throws CacheConnectionException, SerializationException, UnloadableConfigException, InvalidPropertyException;
    
    public abstract Boolean delete(Integer entityId) throws CacheConnectionException;
    
    public abstract Boolean exists(Integer entityId) throws CacheConnectionException;
}