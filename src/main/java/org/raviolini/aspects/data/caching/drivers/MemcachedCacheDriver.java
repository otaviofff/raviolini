package org.raviolini.aspects.data.caching.drivers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.domain.Entity;

import net.spy.memcached.MemcachedClient;

public class MemcachedCacheDriver<T extends Entity> extends AbstractCacheDriver<T> {

    private MemcachedClient cache;
    
    public MemcachedCacheDriver(String host, Integer port, String pass) {
        super(host, port, pass);
    }
    
    private MemcachedClient getCache() throws IOException {
        if (cache == null) {
            InetSocketAddress address = new InetSocketAddress(getHost(), getPort());
            cache = new MemcachedClient(address);
        }

        return cache;
    }
    
    @Override
    public void disconnect() {
        if (cache != null) {
            cache.shutdown();
        }
    }

    @Override
    protected String doGet(String key) throws CacheConnectionException {
        try {
            return getCache().get(key).toString();
        } catch (IOException | RuntimeException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doSet(String key, String value) throws CacheConnectionException {
        try {
            return getCache().set(key, 0, value).get();
        } catch (IOException | InterruptedException | ExecutionException | RuntimeException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doDelete(String key) throws CacheConnectionException {
        try {
            return getCache().delete(key).get();
        } catch (IOException | InterruptedException | ExecutionException | RuntimeException e) {
            throw new CacheConnectionException(e);
        }
    }

    @Override
    protected Boolean doExists(String key) throws CacheConnectionException {
        try {
            return getCache().append(key, "").get();
        } catch (IOException | InterruptedException | ExecutionException | RuntimeException e) {
            throw new CacheConnectionException(e);
        }
    }
}