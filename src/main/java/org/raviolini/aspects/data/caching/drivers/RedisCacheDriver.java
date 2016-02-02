package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.domain.Entity;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisException;

public class RedisCacheDriver<T extends Entity> extends AbstractCacheDriver<T> {

    private Jedis cache;
    
    public RedisCacheDriver(String host, Integer port, String pass) {
        super(host, port, pass);
    }

    private Jedis getCache() {
        if (cache == null) {
            JedisShardInfo info = new JedisShardInfo(getHost(), getPort());
            info.setPassword(getPassword());
            cache = new Jedis(info);
        }
        
        return cache;
    }
    
    @Override
    public void disconnect() {
        if (cache != null) {
            cache.disconnect();
        }
    }

    @Override
    protected String doGet(String key) throws CacheConnectionException {
        try {
            return getCache().get(key);
        } catch (JedisException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doSet(String key, String value) throws CacheConnectionException {
        try {
            return getCache().set(key, value).equals("OK");
        } catch (JedisException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doDelete(String key) throws CacheConnectionException {
        try {
            return getCache().del(key) > 0;
        } catch (JedisException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doExists(String key) throws CacheConnectionException {
        try {
            return getCache().exists(key);
        } catch (JedisException | IllegalArgumentException e) {
            throw new CacheConnectionException(e);
        }
    }
}