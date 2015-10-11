package org.raviolini.services.db;

import java.io.IOException;
import java.util.Map;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.services.SerializationService;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class CachingService<T extends Entity> extends PersistenceService {

    private Jedis cache;
    private SerializationService<T> serializer;
    
    public CachingService() {
        setConfigNamespace("raviolini.cache");
        setConfigKeys(new String[] {"host", "port"});
    }
    
    private Jedis getCache() throws IOException {
        if (cache == null) {
            Map<String, String> properties = getProperties();
            
            String host = properties.get("host");
            Integer port = Integer.valueOf(properties.get("port"));
            
            cache = new Jedis(host, port);
        }
        
        return cache;
    }
    
    private SerializationService<T> getSerializer() {
        if (serializer == null) {
            serializer = new SerializationService<>();
        }
        
        return serializer;
    }
    
    public Boolean set(T entity, Class<T> entityClass) throws InternalServerException {
        String key = String.valueOf(entity.getId());

        try {
            String value = getSerializer().serialize(entity);
            getCache().set(key, value);
            
            return true;
        } catch (JedisConnectionException e) {
            logException(e);
            return false;
        } catch (JsonGenerationException | JsonMappingException e) {
            logException(e);
            throw new InternalServerException("Cannot serialize entity to be cached.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Cache config failed upon SET.");
        }
    }
    
    public T get(Integer id, Class<T> entityClass) throws InternalServerException {
        String key = String.valueOf(id);

        try {
            String value = getCache().get(key);
            
            return getSerializer().unserialize(value, entityClass);
        } catch (JedisConnectionException e) {
            logException(e);
            return null;
        } catch (JsonParseException | JsonMappingException e) {
            logException(e);
            throw new InternalServerException("Cannot unserialize entity cached.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Cache config failed upon GET.");
        }
    }
    
    public Boolean delete(Integer id) throws InternalServerException {
        try {
            String key = String.valueOf(id);
            getCache().del(key);
            
            return true;
        } catch (JedisConnectionException e) {
            logException(e);
            return false;
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Cache config failed upon DEL.");
        }
    }
    
    public Boolean exists(Integer id) throws InternalServerException {
        try {
            String key = String.valueOf(id);
            
            return getCache().exists(key);
        } catch (JedisConnectionException e) {
            logException(e);
            return false;
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Cache config failed.");
        }
    }
}