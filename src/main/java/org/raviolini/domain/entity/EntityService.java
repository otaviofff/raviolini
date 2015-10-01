package org.raviolini.domain.entity;

import java.util.List;

import org.raviolini.api.exception.InternalServerException;

public class EntityService<T extends Entity> {

    //TODO: Implement caching.
    
    private EntityRepository<T> repository;
    
    private EntityRepository<T> getRepository() {
        if (repository == null) {
            repository = new EntityRepository<T>();
        }
        
        return repository;
    }
    
    public List<T> get(Class<T> entityClass) throws InternalServerException {
        return getRepository().select(entityClass);
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws InternalServerException {
        return getRepository().select(entityId, entityClass);
    }
    
    public void post(T entity, Class<T> entityClass) throws InternalServerException {
        getRepository().insert(entity, entityClass);
    }
    
    public void put(T entity, Class<T> entityClass) throws InternalServerException {
        getRepository().update(entity, entityClass);
    }
    
    public void delete(Integer entityId, Class<T> entityClass) throws InternalServerException {
        getRepository().delete(entityId, entityClass);        
    }
}