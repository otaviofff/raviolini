package org.raviolini.services;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.raviolini.domain.Entity;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationService<T extends Entity> {
    
    public T unserialize(String payload, Class<T> entityClass) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        return mapper.readValue(payload, entityClass);
    }
    
    public String serialize(T entity) throws JsonGenerationException, JsonMappingException, IOException {
        return serializeObject(entity);
    }
    
    public String serialize(List<T> list) throws JsonGenerationException, JsonMappingException, IOException {
        return serializeObject(list);
    }
    
    private String serializeObject(Object object) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        
        mapper.writeValue(writer, object);
        
        return writer.toString();
    }
}