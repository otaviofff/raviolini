package org.raviolini.api.adapters;

import java.io.IOException;
import java.util.List;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.domain.Entity;
import org.raviolini.domain.EntityService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        String body, type;
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>(); 

        List<T> list = service.get(entityClass);
        
        try {
            body = serializer.serialize(list);
            type = serializer.getContentType();
        } catch (IOException e) {
            throw new InternalServerException("Cannot serialize the object stored.");
        }
        
        response.status(200);
        response.body(body);
        response.type(type);

        return response;
    }
}