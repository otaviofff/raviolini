package org.raviolini.facade;

import java.util.List;

import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.ReadOperationException;
import org.raviolini.facade.exceptions.WriteOperationException;

public abstract class AbstractService<T extends Entity> {

    public abstract List<T> get(Class<T> entityClass) throws ReadOperationException;
    
    public abstract T get(Integer entityId, Class<T> entityClass) throws ReadOperationException;
    
    public abstract void post(T entity, Class<T> entityClass) throws WriteOperationException;
    
    public abstract void put(T entity, Class<T> entityClass) throws WriteOperationException;
    
    public abstract void delete(Integer entityId, Class<T> entityClass) throws WriteOperationException;
    
    protected void hookOnList(List<T> list) {}
    
    protected void hookOnGet(T entity) {}
    
    protected void hookOnPost(T entity) {}
    
    protected void hookOnPut(T entity) {}
    
    protected void kookOnDelete(Integer entityId) {}
}