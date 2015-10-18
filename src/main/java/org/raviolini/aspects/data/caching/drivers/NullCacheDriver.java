package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.domain.Entity;

public class NullCacheDriver<T extends Entity> extends CacheDriver<T> {

    public NullCacheDriver(String host, Integer port) {
        super(host, port);
    }

    @Override
    public T get(Integer entityId, Class<T> entityClass) {
        return null;
    }

    @Override
    public Boolean set(T entity, Class<T> entityClass) {
        return true;
    }

    @Override
    public Boolean delete(Integer entityId) {
        return true;
    }

    @Override
    public Boolean exists(Integer entityId) {
        return false;
    }
}