package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.service.EntityService;

import spark.Request;
import spark.Response;

public class PostRequestAdapter<T extends Entity> extends WriteRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws BadRequestException, InternalServerException {
        T entity = unserializeRequestBody(request, entityClass);
        
        EntityService<T> service = new EntityService<>();
        service.post(entity, entityClass);
        
        response.body("");
        response.status(201);
        response.type("application/json");
        
        return response;
    }
}