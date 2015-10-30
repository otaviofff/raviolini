package org.raviolini.aspects.io.serialization;

import java.util.List;

import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public class SerializationService<T extends Entity> {
    
    private AbstractSerializationDriver<T> driver;
    
    private AbstractSerializationDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        if (driver == null) {
            driver = SerializationFactory.getDriver();
        }
        
        return driver;
    }
    
    public T unserialize(String entitySerialized, Class<T> entityClass) throws UnserializationException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().unserialize(entitySerialized, entityClass);
    }
    
    public String serialize(T entity) throws SerializationException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().serialize(entity);
    }
    
    public String serialize(List<T> list) throws SerializationException, UnloadableConfigException, InvalidPropertyException {
        return getDriver().serialize(list);
    }
}