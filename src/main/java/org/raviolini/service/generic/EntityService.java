package org.raviolini.service.generic;

import java.util.List;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;

public class EntityService<T extends Entity> {

    private DatabaseService<T> repository;
    private CachingService<T> cache;
    
    private DatabaseService<T> getDatabase() {
        if (repository == null) {
            repository = new DatabaseService<T>();
        }
        
        return repository;
    }
    
    private CachingService<T> getCache() {
        if (cache == null) {
            cache = new CachingService<>();
        }
        
        return cache;
    }
    
    public List<T> get(Class<T> entityClass) throws InternalServerException {
        return getDatabase().select(entityClass);
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws InternalServerException, BadRequestException {
        if (getCache().exists(entityId)) {
            return getCache().get(entityId, entityClass);
        }
        
        T entity = getDatabase().select(entityId, entityClass);
        getCache().set(entity, entityClass);
        
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