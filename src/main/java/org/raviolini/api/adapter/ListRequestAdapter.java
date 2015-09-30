package org.raviolini.api.adapter;

import java.util.List;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.dog.Dog;
import org.raviolini.domain.dog.EntitySerializer;
import org.raviolini.domain.dog.EntityService;

import spark.Request;
import spark.Response;

public class ListRequestAdapter extends ReadRequestAdaptar {

    @Override
    public Response handle(Request request, Response response) throws InternalServerException {
        List<Dog> list = EntityService.get();
        String body = EntitySerializer.serialize(list);
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}