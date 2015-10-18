package org.raviolini.api.adapters;

import java.io.IOException;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.aspects.data.validation.ValidationService;
import org.raviolini.aspects.data.validation.exceptions.InvalidEntityException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.domain.Entity;

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
        } catch (InvalidEntityException e) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}