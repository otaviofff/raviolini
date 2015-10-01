package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.domain.entity.Entity;
import org.raviolini.domain.entity.EntitySerializer;
import org.raviolini.domain.entity.EntityValidator;

import spark.Request;

public abstract class WriteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    protected T unserializeRequestBody(Request request, Class<T> entityClass) throws BadRequestException {
        EntitySerializer<T> serializer = new EntitySerializer<>();
        EntityValidator validator = new EntityValidator();
        
        T entity = serializer.unserialize(request.body(), entityClass);
        validator.validate(entity);
        
        return entity;
    }
}