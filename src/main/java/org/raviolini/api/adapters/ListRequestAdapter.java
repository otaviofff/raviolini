package org.raviolini.api.adapters;

import java.util.List;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.domain.Entity;
import org.raviolini.domain.EntityService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        String body = "";
        String type = request.headers("Accept");
        EntityService<T> service = new EntityService<>();
        SerializationService<T> serializer = new SerializationService<>(type); 

        List<T> list = service.get(entityClass);
        
        try {
            body = serializer.serialize(list);
        } catch (SerializationException e) {
            throw new InternalServerException("Cannot serialize the objects stored.");
        }
        
        response.status(200);
        response.body(body);
        response.type(type);

        return response;
    }
}