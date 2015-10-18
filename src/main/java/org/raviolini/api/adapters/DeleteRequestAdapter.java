package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.domain.EntityService;

import spark.Request;
import spark.Response;

public class DeleteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        Integer entityId = Integer.valueOf(request.params("id"));
        
        EntityService<T> service = new EntityService<>();
        service.delete(entityId, entityClass);
        
        response.body("");
        response.status(200);
        response.type("application/json");
        
        return response;
    }
}