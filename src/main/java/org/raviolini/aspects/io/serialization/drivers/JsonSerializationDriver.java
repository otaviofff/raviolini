package org.raviolini.aspects.io.serialization.drivers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializationDriver<T extends Entity> extends AbstractSerializationDriver<T> {

    @Override
    public T unserialize(String entitySerialized, Class<T> entityClass) throws UnserializationException {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            return mapper.readValue(entitySerialized, entityClass);
        } catch (IOException e) {
            throw new UnserializationException();
        }
    }

    @Override
    public String serialize(T entity) throws SerializationException {
        return serializeObject(entity);
    }

    @Override
    public String serialize(List<T> list) throws SerializationException {
        return serializeObject(list);
    }

    private String serializeObject(Object object) throws SerializationException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        
        try {
            mapper.writeValue(writer, object);
        } catch (IOException e) {
            throw new SerializationException();
        }
        
        return writer.toString();
    }
}