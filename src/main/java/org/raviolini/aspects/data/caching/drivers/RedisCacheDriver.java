package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.domain.Entity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisCacheDriver<T extends Entity> extends AbstractCacheDriver<T> {

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
    protected String doGet(String key) throws CacheConnectionException {
        try {
            return getCache().get(key);
        } catch (JedisConnectionException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        } finally {
            getCache().close();
        }
    }

    @Override
    protected Boolean doSet(String key, String value) throws CacheConnectionException {
        try {
            return getCache().set(key, value).equals("OK");
        } catch (JedisConnectionException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        } finally {
            getCache().close();
        }
    }

    @Override
    protected Boolean doDelete(String key) throws CacheConnectionException {
        try {
            return getCache().del(key) > 0;
        } catch (JedisConnectionException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        } finally {
            getCache().close();
        }
    }

    @Override
    protected Boolean doExists(String key) throws CacheConnectionException {
        try {
            return getCache().exists(key);
        } catch (JedisConnectionException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        } finally {
            getCache().close();
        }
    }
}