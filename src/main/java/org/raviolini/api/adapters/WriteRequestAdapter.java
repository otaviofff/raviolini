package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.aspects.data.validation.ValidationService;
import org.raviolini.aspects.data.validation.exceptions.InvalidEntityException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

import spark.Request;

public abstract class WriteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    protected T unserializeRequestBody(Request request, Class<T> entityClass) throws BadRequestException {
        String type = request.headers("Content-Type");
        SerializationService<T> serializer = new SerializationService<>(type);
        ValidationService validator = new ValidationService();
        
        try {
            T entity = serializer.unserialize(request.body(), entityClass);
            validator.validate(entity);
            
            return entity;
        } catch (UnserializationException e) {
            throw new BadRequestException("Cannot unserialize the payload given.");
        } catch (InvalidEntityException e) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}