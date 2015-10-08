package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.domain.Entity;
import org.raviolini.service.generic.SerializationService;
import org.raviolini.service.generic.ValidationService;

import spark.Request;

public abstract class WriteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    protected T unserializeRequestBody(Request request, Class<T> entityClass) throws BadRequestException {
        SerializationService<T> serializer = new SerializationService<>();
        ValidationService validator = new ValidationService();
        
        T entity = serializer.unserialize(request.body(), entityClass);
        validator.validate(entity);
        
        return entity;
    }
}