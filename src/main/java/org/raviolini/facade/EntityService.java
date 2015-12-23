package org.raviolini.facade;

import java.util.List;

import org.raviolini.aspects.data.caching.CacheService;
import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.data.database.DatabaseService;
import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.logging.LogService;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.ReadOperationException;
import org.raviolini.facade.exceptions.WriteOperationException;

public class EntityService<T extends Entity> extends AbstractService<T> {

    private DatabaseService<T> database;
    private CacheService<T> cache;
    private LogService log;
    
    protected DatabaseService<T> getDatabase() {
        if (database == null) {
            database = new DatabaseService<T>();
        }
        
        return database;
    }
    
    protected CacheService<T> getCache() {
        if (cache == null) {
            cache = new CacheService<T>();
        }
        
        return cache;
    }
    
    protected LogService getLog() {
        if (log == null) {
            log = new LogService();
        }
        
        return log;
    }
    
    public final List<T> get(Class<T> entityClass) throws ReadOperationException {
        List<T> list;
        
        try {
            list = getDatabase().select(entityClass);
        } catch (UnloadableConfigException 
                | DatabaseCommandException 
                | InvalidPropertyException e) {
            throw new ReadOperationException("Failed to get entity list.", e);
        }
        
        hookOnList();
        
        return list;
    }
    
    public final T get(Integer entityId, Class<T> entityClass) throws ReadOperationException {
        T entity;
        
        try {
            if (getCache().exists(entityId)) {
                return getCache().get(entityId, entityClass);
            }
            
            entity = getDatabase().select(entityId, entityClass);
            
            if (entity != null) {
                getCache().set(entity, entityClass);
            }
        } catch (UnloadableConfigException 
                | InvalidPropertyException 
                | CacheConnectionException
                | DatabaseCommandException
                | UnserializationException 
                | SerializationException e) {
            throw new ReadOperationException("Failed to get entity.", e);
        }
        
        hookOnGet();
        
        return entity;
    }
    
    public final void post(T entity, Class<T> entityClass) throws WriteOperationException {
        try {
            getDatabase().insert(entity, entityClass);
            getCache().set(entity, entityClass);
        } catch (UnloadableConfigException 
                | InvalidPropertyException 
                | CacheConnectionException
                | DatabaseCommandException
                | SerializationException e) {
            throw new WriteOperationException("Failed to post entity.", e);
        }
        
        hookOnPost();
    }
    
    public final void put(T entity, Class<T> entityClass) throws WriteOperationException {
        try {
            getDatabase().update(entity, entityClass);
            getCache().set(entity, entityClass);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException
                | CacheConnectionException
                | SerializationException e) {
            throw new WriteOperationException("Failed to put entity.", e);
        }
        
        hookOnPut();
    }
    
    public final void delete(Integer entityId, Class<T> entityClass) throws WriteOperationException {
        try {
            getDatabase().delete(entityId, entityClass);
            getCache().delete(entityId);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException
                | CacheConnectionException e) {
            throw new WriteOperationException("Failed to delete entity.", e);
        }
        
        kookOnDelete();
    }
}