package org.raviolini.api.adapter;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.api.exception.NotFoundException;
import org.raviolini.domain.Entity;
import org.raviolini.service.generic.EntityService;
import org.raviolini.service.generic.SerializationService;

import spark.Request;
import spark.Response;

public class GetRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {
    
    @Override
    public Response handle(Request request, Response response, Class<T> entityCLass) throws InternalServerException, NotFoundException {
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>();
        
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