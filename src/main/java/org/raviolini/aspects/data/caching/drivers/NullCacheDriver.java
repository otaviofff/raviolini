package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.domain.Entity;

public class NullCacheDriver<T extends Entity> extends AbstractCacheDriver<T> {

    public NullCacheDriver(String host, Integer port) {
        super(host, port);
    }

    @Override
    protected String doGet(String key) throws CacheConnectionException {
        return null;
    }

    @Override
    protected Boolean doSet(String key, String value) throws CacheConnectionException {
        return true;
    }

    @Override
    protected Boolean doDelete(String key) throws CacheConnectionException {
        return true;
    }

    @Override
    protected Boolean doExists(String key) throws CacheConnectionException {
        return false;
    }
}