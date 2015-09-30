package org.raviolini.domain.dog;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EntitySerializer {
    
    public static Dog unserialize(String payload) throws BadRequestException {
        ObjectMapper mapper = new ObjectMapper();
        Dog entity;
        
        try {
            entity = mapper.readValue(payload, Dog.class);
        } catch (IOException e) {
            logException(e);
            throw new BadRequestException("Cannot unserialize the payload given.");
        }
        
        return entity;
    }
    
    public static String serialize(Dog entity) throws InternalServerException {
        return serializeObject(entity);
    }
    
    public static String serialize(List<Dog> list) throws InternalServerException {
        return serializeObject(list);
    }
    
    private static String serializeObject(Object object) throws InternalServerException {
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
    
    private static void logException(Exception e) {
        //TODO: Implement logging.
        e.printStackTrace();
    }
}