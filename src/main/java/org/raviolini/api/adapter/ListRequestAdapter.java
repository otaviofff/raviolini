package org.raviolini.api.adapter;

import java.io.IOException;
import java.util.List;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.services.EntityService;
import org.raviolini.services.SerializationService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        String body;
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>(); 

        List<T> list = service.get(entityClass);
        
        try {
            body = serializer.serialize(list);
        } catch (IOException e) {
            throw new InternalServerException("Cannot serialize the object stored.");
        }
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}