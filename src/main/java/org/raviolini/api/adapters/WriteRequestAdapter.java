package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.aspects.data.validation.ValidationService;
import org.raviolini.aspects.data.validation.exceptions.InvalidEntityException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Entity;

import spark.Request;

public abstract class WriteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    private ValidationService validator;
    
    private ValidationService getValidator() {
        if (validator == null) {
            validator = new ValidationService();
        }
        
        return validator;
    }
    
    protected T unserializeRequestBody(Request request, Class<T> entityClass) throws BadRequestException {
        String body = request.body();
        String mediaType = request.headers("Content-Type");
        
        T entity = null;
        
        try {
            entity = getSerializer(mediaType).unserialize(body, entityClass);
            getValidator().validate(entity);
        } catch (UnserializationException e) {
            throw new BadRequestException(e);
        } catch (InvalidEntityException e) {
            throw new BadRequestException(e);
        }
        
        return entity;
    }
}