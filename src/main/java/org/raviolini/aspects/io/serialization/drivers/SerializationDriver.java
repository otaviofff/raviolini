package org.raviolini.aspects.io.serialization.drivers;

import java.util.List;

import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

public abstract class SerializationDriver<T extends Entity> {
    
    public abstract T unserialize(String entitySerialized, Class<T> entityClass) throws UnserializationException;
    
    public abstract String serialize(T entity) throws SerializationException;
    
    public abstract String serialize(List<T> list) throws SerializationException;
}