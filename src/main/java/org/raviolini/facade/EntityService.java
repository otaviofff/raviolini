package org.raviolini.facade;

import java.util.HashMap;
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
import org.raviolini.facade.exceptions.HookExecutionException;
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
    
    public final List<T> get(HashMap<String, String> params, Class<T> entityClass) throws ReadOperationException, HookExecutionException {
        hookBeforeList();
        
        List<T> list;
        
        try {
            list = getDatabase().select(params, entityClass);
        } catch (UnloadableConfigException 
                | DatabaseCommandException 
                | InvalidPropertyException e) {
            throw new ReadOperationException("Failed to get entity list.", e);
        }
        
        hookAfterList(list);
        
        return list;
    }
    
    public final T get(Integer entityId, Class<T> entityClass) throws ReadOperationException, HookExecutionException {
        hookBeforeGet(entityId);
        
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
        } finally {
            getCache().disconnect();
        }
        
        hookAfterGet(entity);
        
        return entity;
    }
    
    public final void post(T entity, Class<T> entityClass) throws WriteOperationException, HookExecutionException {
        hookBeforePost(entity);
        
        try {
            getDatabase().insert(entity, entityClass);
            getCache().set(entity, entityClass);
        } catch (UnloadableConfigException 
                | InvalidPropertyException 
                | CacheConnectionException
                | DatabaseCommandException
                | SerializationException e) {
            throw new WriteOperationException("Failed to post entity.", e);
        } finally {
            getCache().disconnect();
        }
        
        hookAfterPost(entity);
    }
    
    public final void put(T entity, Class<T> entityClass) throws WriteOperationException, HookExecutionException {
        hookBeforePut(entity);
        
        try {
            getDatabase().update(entity, entityClass);
            getCache().set(entity, entityClass);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException
                | CacheConnectionException
                | SerializationException e) {
            throw new WriteOperationException("Failed to put entity.", e);
        } finally {
            getCache().disconnect();
        }
        
        hookAfterPut(entity);
    }
    
    public final void delete(Integer entityId, Class<T> entityClass) throws WriteOperationException, HookExecutionException {
        hookBeforeDelete(entityId);
        
        try {
            getDatabase().delete(entityId, entityClass);
            getCache().delete(entityId);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException
                | CacheConnectionException e) {
            throw new WriteOperationException("Failed to delete entity.", e);
        } finally {
            getCache().disconnect();
        }
        
        hookAfterDelete(entityId);
    }

    @Override
    public Long count(HashMap<String, String> params, Class<T> entityClass) throws ReadOperationException {
        try {
            return getDatabase().count(params, entityClass);
        } catch (UnloadableConfigException 
                | InvalidPropertyException
                | DatabaseCommandException e) {
            throw new ReadOperationException("Failed to count entities stored.", e);
        }
    }
}