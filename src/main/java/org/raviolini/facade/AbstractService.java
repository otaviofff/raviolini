package org.raviolini.facade;

import java.util.HashMap;
import java.util.List;

import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.ReadOperationException;
import org.raviolini.facade.exceptions.WriteOperationException;

public abstract class AbstractService<T extends Entity> {

    public abstract List<T> get(HashMap<String, String> params, Class<T> entityClass) throws ReadOperationException, HookExecutionException;
    
    public abstract T get(Integer entityId, Class<T> entityClass) throws ReadOperationException, HookExecutionException;
    
    public abstract void post(T entity, Class<T> entityClass) throws WriteOperationException, HookExecutionException;
    
    public abstract void put(T entity, Class<T> entityClass) throws WriteOperationException, HookExecutionException;
    
    public abstract void delete(Integer entityId, Class<T> entityClass) throws WriteOperationException, HookExecutionException;
    
    /*** Pre-Execution Hooks ***/
    
    protected void hookBeforeList() throws HookExecutionException {};
    
    protected void hookBeforeGet(Integer entityId) throws HookExecutionException {};
    
    protected void hookBeforePost(T entity) throws HookExecutionException {};
    
    protected void hookBeforePut(T entity) throws HookExecutionException {};
    
    protected void hookBeforeDelete(Integer entityId) throws HookExecutionException {};
    
    /*** Post-Execution Hooks ***/
    
    protected void hookAfterList(List<T> list) throws HookExecutionException {}
    
    protected void hookAfterGet(T entity) throws HookExecutionException {}
    
    protected void hookAfterPost(T entity) throws HookExecutionException {}
    
    protected void hookAfterPut(T entity) throws HookExecutionException {}
    
    protected void hookAfterDelete(Integer entityId) throws HookExecutionException {}
}