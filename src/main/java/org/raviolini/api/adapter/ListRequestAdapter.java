package org.raviolini.api.adapter;

import java.util.List;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.service.generic.EntityService;
import org.raviolini.service.generic.SerializationService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>(); 
        
        List<T> list = service.get(entityClass);
        String body = serializer.serialize(list);
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}