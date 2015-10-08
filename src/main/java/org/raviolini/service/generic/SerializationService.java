package org.raviolini.service.generic;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.service.concrete.LoggingService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SerializationService<T extends Entity> {
    
    public T unserialize(String payload, Class<T> entityClass) throws BadRequestException {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            return mapper.readValue(payload, entityClass);
        } catch (IOException e) {
            logException(e);
            throw new BadRequestException("Cannot unserialize the payload given.");
        }
    }
    
    public String serialize(T entity) throws InternalServerException {
        return serializeObject(entity);
    }
    
    public String serialize(List<T> list) throws InternalServerException {
        return serializeObject(list);
    }
    
    private String serializeObject(Object object) throws InternalServerException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        
        try {
            mapper.writeValue(writer, object);
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Cannot serialize the object stored.");
        }
        
        return writer.toString();
    }
    
    private void logException(Exception e) {
        LoggingService logger = new LoggingService();
        logger.logException(e, true);
    }
}