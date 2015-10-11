package org.raviolini.services;

import java.util.List;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.services.db.CachingService;
import org.raviolini.services.db.DatabaseService;

public class EntityService<T extends Entity> {

    private DatabaseService<T> database;
    private CachingService<T> cache;
    
    private DatabaseService<T> getDatabase() {
        if (database == null) {
            database = new DatabaseService<T>();
        }
        
        return database;
    }
    
    private CachingService<T> getCache() {
        if (cache == null) {
            cache = new CachingService<>();
        }
        
        return cache;
    }
    
    public List<T> get(Class<T> entityClass) throws InternalServerException {
        //TODO: Implement list filtering and sorting.
        return getDatabase().select(entityClass);
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws InternalServerException, BadRequestException {
        if (getCache().exists(entityId)) {
            return getCache().get(entityId, entityClass);
        }
        
        T entity = getDatabase().select(entityId, entityClass);
        
        if (entity != null) {
            getCache().set(entity, entityClass);
        }
        
        return entity;
    }
    
    public void post(T entity, Class<T> entityClass) throws InternalServerException {
        getDatabase().insert(entity, entityClass);
        getCache().set(entity, entityClass);
    }
    
    public void put(T entity, Class<T> entityClass) throws InternalServerException {
        getDatabase().update(entity, entityClass);
        getCache().set(entity, entityClass);
    }
    
    public void delete(Integer entityId, Class<T> entityClass) throws InternalServerException {
        getDatabase().delete(entityId, entityClass);     
        getCache().delete(entityId);
    }
}