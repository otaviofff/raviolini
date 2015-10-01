package org.raviolini.api.adapter;

import java.util.List;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.entity.Entity;
import org.raviolini.domain.entity.EntitySerializer;
import org.raviolini.domain.entity.EntityService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        EntityService<T> service = new EntityService<>();
        EntitySerializer<T> serializer = new EntitySerializer<>(); 
        
        List<T> list = service.get(entityClass);
        String body = serializer.serialize(list);
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}