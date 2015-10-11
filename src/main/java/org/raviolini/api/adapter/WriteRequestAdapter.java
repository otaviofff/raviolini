package org.raviolini.api.adapter;

import java.io.IOException;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.domain.Entity;
import org.raviolini.service.SerializationService;
import org.raviolini.service.ValidationService;

import spark.Request;

public abstract class WriteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    protected T unserializeRequestBody(Request request, Class<T> entityClass) throws BadRequestException {
        SerializationService<T> serializer = new SerializationService<>();
        ValidationService validator = new ValidationService();
        
        try {
            T entity = serializer.unserialize(request.body(), entityClass);
            validator.validate(entity);
            
            return entity;
        } catch (IOException e) {
            throw new BadRequestException("Cannot unserialize the payload given.");
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}