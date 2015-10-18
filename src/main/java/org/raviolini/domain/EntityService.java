package org.raviolini.domain;

import java.util.List;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.aspects.data.caching.CacheService;
import org.raviolini.aspects.data.caching.exceptions.CacheConnectionException;
import org.raviolini.aspects.data.database.DatabaseService;
import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.logging.LogService;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;

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
            cache = new CacheService<>();
        }
        
        return cache;
    }
    
    protected LogService getLog() {
        if (log == null) {
            log = new LogService();
        }
        
        return log;
    }
    
    public List<T> get(Class<T> entityClass) throws InternalServerException {
        //TODO: Implement list filtering and sorting.
        try {
            return getDatabase().select(entityClass);
        } catch (DatabaseCommandException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity from database.");
        } catch (UnloadableConfigException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to unloadable config file.");
        } catch (InvalidPropertyException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to invalid config property.");
        }
    }
    
    public T get(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            if (getCache().exists(entityId)) {
                return getCache().get(entityId, entityClass);
            }
            
            T entity = getDatabase().select(entityId, entityClass);
            
            if (entity != null) {
                getCache().set(entity, entityClass);
            }
            
            return entity;
        } catch (CacheConnectionException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to cache connectivity issues.");
        } catch (UnloadableConfigException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to unloadable config file.");
        } catch (InvalidPropertyException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to invalid cache property.");
        } catch (UnserializationException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity due to unserialization issues.");
        } catch (DatabaseCommandException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to fetch entity from database.");
        } catch (SerializationException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to cache entity due to serialization issues.");
        }
    }
    
    public void post(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            getDatabase().insert(entity, entityClass);
            getCache().set(entity, entityClass);
        } catch (CacheConnectionException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity due to cache connectivity issues.");
        } catch (DatabaseCommandException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity to database.");
        } catch (UnloadableConfigException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity due to unloadable config file.");
        } catch (InvalidPropertyException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity due to invalid config property.");
        } catch (SerializationException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity due to serializaion issues.");
        }
    }
    
    public void put(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            getDatabase().update(entity, entityClass);
            getCache().set(entity, entityClass);
        }
        catch (DatabaseCommandException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to put entity to database.");
        } catch (UnloadableConfigException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to put entity due to unloadable config file.");
        } catch (InvalidPropertyException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to put entity due to invalid config property.");
        } catch (CacheConnectionException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to put entity due to cache connectivity issues.");
        } catch (SerializationException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to post entity due to serialization issues.");
        }
    }
    
    public void delete(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            getDatabase().delete(entityId, entityClass);     
            getCache().delete(entityId);
        } catch (DatabaseCommandException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to delete entity from database.");
        } catch (UnloadableConfigException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to delete entity due to unloadable config file.");
        } catch (InvalidPropertyException e) {
            getLog().logException(e, true);
            throw new InternalServerException("Failed to delete entity due to invalid config property.");
        }
    }
}