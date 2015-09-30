package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.domain.dog.Dog;
import org.raviolini.domain.dog.EntitySerializer;
import org.raviolini.domain.dog.EntityValidator;

import spark.Request;

public abstract class WriteRequestAdapter extends AbstractRequestAdapter {

    protected Dog readRequestBody(Request request) throws BadRequestException {
        Dog entity = EntitySerializer.unserialize(request.body());
        EntityValidator.validate(entity);
        
        return entity;
    }
}