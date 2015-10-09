package org.raviolini.service.generic;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;

import redis.clients.jedis.Jedis;

public class CachingService<T extends Entity> {

    private Jedis cache;
    private SerializationService<T> serializer;
    
    private Jedis getCache() {
        if (cache == null) {
            cache = new Jedis("localhost", 16379);
        }
        
        return cache;
    }
    
    private SerializationService<T> getSerializer() {
        if (serializer == null) {
            serializer = new SerializationService<>();
        }
        
        return serializer;
    }
    
    public void set(T entity, Class<T> entityClass) throws InternalServerException {
        String key = String.valueOf(entity.getId());
        String value = getSerializer().serialize(entity);
        
        getCache().set(key, value);
    }
    
    public T get(Integer id, Class<T> entityClass) throws BadRequestException {
        String key = String.valueOf(id);
        String value = getCache().get(key);

        return getSerializer().unserialize(value, entityClass);
    }
    
    public void delete(Integer id) {
        String key = String.valueOf(id);
        
        getCache().del(key);
    }
    
    public Boolean exists(Integer id) {
        String key = String.valueOf(id);
        
        return getCache().exists(key);
    }
}