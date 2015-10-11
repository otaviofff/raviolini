package org.raviolini.api.adapter;

import java.io.IOException;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.api.exception.NotFoundException;
import org.raviolini.domain.Entity;
import org.raviolini.services.EntityService;
import org.raviolini.services.SerializationService;

import spark.Request;
import spark.Response;

public class GetRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {
    
    @Override
    public Response handle(Request request, Response response, Class<T> entityCLass) throws InternalServerException, NotFoundException, BadRequestException {
        String body;
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>();
        
        T entity = service.get(Integer.valueOf(request.params("id")), entityCLass);
        
        if (entity == null) {
            throw new NotFoundException();
        }
        
        try {
            body = serializer.serialize(entity);
        } catch (IOException e) {
            throw new InternalServerException("Cannot serialize the object stored.");
        }
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}