package org.raviolini.aspects.io.serialization;

import java.util.List;

import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public class SerializationService<T extends Entity> {
    
    private String mediaType;
    private AbstractSerializationDriver<T> driver;

    public SerializationService(String mediaType) {
        this.mediaType = mediaType;
    }
    
    private AbstractSerializationDriver<T> getDriver() {
        if (driver == null) {
            driver = SerializationFactory.getDriver(mediaType);
        }
        
        return driver;
    }
    
    public T unserialize(String entitySerialized, Class<T> entityClass) throws UnserializationException {
        return getDriver().unserialize(entitySerialized, entityClass);
    }
    
    public String serialize(T entity) throws SerializationException {
        return getDriver().serialize(entity);
    }
    
    public String serialize(List<T> list) throws SerializationException {
        return getDriver().serialize(list);
    }
}