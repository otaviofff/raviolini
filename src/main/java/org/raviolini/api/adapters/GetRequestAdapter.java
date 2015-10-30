package org.raviolini.api.adapters;

import java.io.IOException;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.api.exceptions.NotFoundException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.domain.Entity;
import org.raviolini.domain.EntityService;

import spark.Request;
import spark.Response;

public class GetRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {
    
    @Override
    public Response handle(Request request, Response response, Class<T> entityCLass) throws InternalServerException, NotFoundException, BadRequestException {
        String body = "";
        String type = request.headers("Accept");
        Integer entityId = Integer.valueOf(request.params("id"));
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>(type);
        
        T entity = service.get(entityId, entityCLass);
        
        if (entity == null) {
            throw new NotFoundException();
        }
        
        try {
            body = serializer.serialize(entity);
        } catch (IOException e) {
            throw new InternalServerException("Cannot serialize the object stored.");
        }
        
        response.status(200);
        response.body(body);
        response.type(type);

        return response;
    }
}