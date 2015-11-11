package org.raviolini.aspects.data.caching.drivers;

import org.raviolini.domain.Entity;

public class NullCacheDriver<T extends Entity> extends AbstractCacheDriver<T> {

    public NullCacheDriver(String host, Integer port) {
        super(host, port);
    }

    @Override
    protected String doGet(String key) {
        return null;
    }

    @Override
    protected Boolean doSet(String key, String value) {
        return true;
    }

    @Override
    protected Boolean doDelete(String key) {
        return true;
    }

    @Override
    protected Boolean doExists(String key) {
        return false;
    }
}