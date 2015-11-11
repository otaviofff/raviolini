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

public class EntityService<T extends Entity> {

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
    
    public List<T> get(Class<T> entityClass) throws ReadOperationException {
        //TODO: Implement list filtering, sorting, and pagination.
        try {
            return getDatabase().select(entityClass);
        } catch (UnloadableConfigException 
                | DatabaseCommandException 
                | InvalidPropertyException e) {
            throw new ReadOperationException("Failed to get entity list.", e);
        }
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws ReadOperationException {
        try {
            if (getCache().exists(entityId)) {
                return getCache().get(entityId, entityClass);
            }
            
            T entity = getDatabase().select(entityId, entityClass);
            
            if (entity != null) {
                getCache().set(entity, entityClass);
            }
            
            return entity;
        } catch (UnloadableConfigException 
                | InvalidPropertyException 
                | CacheConnectionException
                | DatabaseCommandException
                | UnserializationException 
                | SerializationException e) {
            throw new ReadOperationException("Failed to get entity.", e);
        }
    }
    
    public void post(T entity, Class<T> entityClass) throws WriteOperationException {
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
    }
    
    public void put(T entity, Class<T> entityClass) throws WriteOperationException {
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
    }
    
    public void delete(Integer entityId, Class<T> entityClass) throws WriteOperationException {
        try {
            getDatabase().delete(entityId, entityClass);
            getCache().delete(entityId);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException
                | CacheConnectionException e) {
            throw new WriteOperationException("Failed to delete entity.", e);
        }
    }
}