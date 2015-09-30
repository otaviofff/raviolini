package org.raviolini.api.adapter;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.api.exception.NotFoundException;
import org.raviolini.domain.dog.Dog;
import org.raviolini.domain.dog.EntitySerializer;
import org.raviolini.domain.dog.EntityService;

import spark.Request;
import spark.Response;

public class GetRequestAdapter extends ReadRequestAdaptar {
    
    @Override
    public Response handle(Request request, Response response) throws InternalServerException, NotFoundException {
        Integer id = Integer.valueOf(request.params("id"));
        Dog entity = EntityService.get(id);
        
        if (entity == null) {
            throw new NotFoundException();
        }
        
        String body = EntitySerializer.serialize(entity);
        
        response.body(body);
        response.status(200);
        response.type("application/json");

        return response;
    }
}