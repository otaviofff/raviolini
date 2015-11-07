package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public abstract class AbstractCacheDriver<T extends Entity> {

    private String host;
    private Integer port;
    private SerializationService<T> serializer;
    
    public AbstractCacheDriver(String host, Integer port) {
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

    /***
     * Loads the serialization service, which will be used to serialize
     *  objects to be cached, as well as unserialize objects previously
     *  cached.
     * 
     * Objects are always cached in JSON, which does not affect how objects
     *  are returned to API clients, which could be in either JSON or XML, 
     *  pending on the HTTP request header Accept.
     * 
     * @return SerializationService
     */
    protected SerializationService<T> getSerializer() {
        if (serializer == null) {
            serializer = new SerializationService<>("application/json");
        }
        
        return serializer;
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws CacheConnectionException, UnserializationException {
        String key = String.valueOf(entityId);
        String value = doGet(key);
        
        if (value == null) {
            return null;
        }
        
        T entity = getSerializer().unserialize(value, entityClass);
        
        return entity;
    }
    
    public Boolean set(T entity, Class<T> entityClass) throws CacheConnectionException, SerializationException {
        String key = String.valueOf(entity.getId());
        String value = getSerializer().serialize(entity);
        
        return doSet(key, value);
    }
    
    public Boolean delete(Integer entityId) throws CacheConnectionException {
        String key = String.valueOf(entityId);

        return doDelete(key);
    }
    
    public Boolean exists(Integer entityId) throws CacheConnectionException {
        String key = String.valueOf(entityId);
        
        return doExists(key);
    }
    
    protected abstract String doGet(String key) throws CacheConnectionException;
    
    protected abstract Boolean doSet(String key, String value) throws CacheConnectionException;
    
    protected abstract Boolean doDelete(String key) throws CacheConnectionException;
    
    protected abstract Boolean doExists(String key) throws CacheConnectionException;
}