package org.raviolini.aspects.io.serialization.drivers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractSerializationDriver<T extends Entity> {
    
    protected abstract ObjectMapper getObjectMapper();

    public abstract String getContentType();
    
    public T unserialize(String entitySerialized, Class<T> entityClass) throws UnserializationException {
        ObjectMapper mapper = getObjectMapper();
        
        try {
            return mapper.readValue(entitySerialized, entityClass);
        } catch (IOException e) {
            throw new UnserializationException();
        }
    }

    public String serialize(T entity) throws SerializationException {
        return serializeObject(entity);
    }

    public String serialize(List<T> list) throws SerializationException {
        return serializeObject(list);
    }

    private String serializeObject(Object object) throws SerializationException {
        ObjectMapper mapper = getObjectMapper();
        StringWriter writer = new StringWriter();
        
        try {
            mapper.writeValue(writer, object);
        } catch (IOException e) {
            throw new SerializationException();
        }
        
        return writer.toString();
    }
}