package org.raviolini.aspects.data.database;

import java.util.HashMap;
import java.util.List;

import org.raviolini.aspects.data.database.drivers.AbstractDatabaseDriver;
import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.domain.Entity;

public class DatabaseService<T extends Entity> {
    
    private AbstractDatabaseDriver<T> driver;
    
    private AbstractDatabaseDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        if (driver == null) {
            driver = DatabaseFactory.getDriver();
        }
        
        return driver;
    }
    
    public List<T> select(HashMap<String, String> params, Class<T> entityClass) throws DatabaseCommandException, UnloadableConfigException, InvalidPropertyException {
        if (params.size() > 0) {
            return getDriver().select(params, entityClass);
        }
        
        return getDriver().select(entityClass);
    }
    
    public T select(Integer entityId, Class<T> entityClass) throws DatabaseCommandException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().select(entityId, entityClass);
    }
    
    public Integer insert(T entity, Class<T> entityClass) throws DatabaseCommandException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().insert(entity, entityClass);
    }
    
    public Integer update(T entity, Class<T> entityClass) throws DatabaseCommandException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().update(entity, entityClass);
    }
    
    public Integer delete(Integer entityId, Class<T> entityClass) throws DatabaseCommandException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().delete(entityId, entityClass);
    }
}