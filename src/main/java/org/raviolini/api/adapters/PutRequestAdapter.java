package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.services.EntityService;

import spark.Request;
import spark.Response;

public class PutRequestAdapter<T extends Entity> extends WriteRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws BadRequestException, InternalServerException {
        T entity = unserializeRequestBody(request, entityClass);
        
        if (entity.getId() != Integer.valueOf(request.params("id"))) {
            throw new BadRequestException("Payload ID and URI ID do not match.");
        }
        
        EntityService<T> service = new EntityService<>();
        service.put(entity, entityClass);
        
        response.body("");
        response.status(200);
        response.type("application/json");
        
        return response;
    }
}