package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisCacheDriver<T extends Entity> extends CacheDriver<T> {

    private Jedis cache;
    
    public RedisCacheDriver(String host, Integer port) {
        super(host, port);
    }

    private Jedis getCache() {
        if (cache == null) {
            cache = new Jedis(getHost(), getPort());
        }
        
        return cache;
    }
    
    @Override
    public T get(Integer entityId, Class<T> entityClass) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException, UnserializationException {
        String key = String.valueOf(entityId);
        String value;
        
        try {
            value = getCache().get(key);
        } catch (JedisConnectionException e) {
            throw new CacheConnectionException();
        }
        
        T entity = getSerializer().unserialize(value, entityClass);
        
        return entity;
    }

    @Override
    public Boolean set(T entity, Class<T> entityClass) throws CacheConnectionException, UnloadableConfigException, InvalidPropertyException, SerializationException {
        String key = String.valueOf(entity.getId());
        String value = getSerializer().serialize(entity);
        
        try {
            getCache().set(key, value);
        } catch (JedisConnectionException e) {
            throw new CacheConnectionException();
        }
       
        return true;
    }

    @Override
    public Boolean delete(Integer entityId) throws CacheConnectionException {
        String key = String.valueOf(entityId);
        
        try {
            getCache().del(key);
        } catch (JedisConnectionException e) {
            throw new CacheConnectionException();
        }

        return true;
    }

    @Override
    public Boolean exists(Integer entityId) throws CacheConnectionException {
        String key = String.valueOf(entityId);
        
        try {
            return getCache().exists(key);
        } catch (JedisConnectionException e) {
            throw new CacheConnectionException();
        }
    }
}