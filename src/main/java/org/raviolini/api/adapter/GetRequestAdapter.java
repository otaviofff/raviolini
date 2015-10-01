package org.raviolini.api.adapter;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.api.exception.NotFoundException;
import org.raviolini.domain.entity.Entity;
import org.raviolini.domain.entity.EntitySerializer;
import org.raviolini.domain.entity.EntityService;

import spark.Request;
import spark.Response;

public class GetRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {
    
    @Override
    public Response handle(Request request, Response response, Class<T> entityCLass) throws InternalServerException, NotFoundException {
        EntityService<T> service = new EntityService<>();
        EntitySerializer<T> serializer = new EntitySerializer<>();
        
        T entity = service.get(Integer.valueOf(request.params("id")), entityCLass);
        
        if (entity == null) {
            throw new NotFoundException();
        }
        
        String body = serializer.serialize(entity);
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}