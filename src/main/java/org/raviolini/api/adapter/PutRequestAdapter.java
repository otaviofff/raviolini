package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.dog.Dog;
import org.raviolini.domain.dog.EntityService;

import spark.Request;
import spark.Response;

public class PutRequestAdapter extends WriteRequestAdapter {

    @Override
    public Response handle(Request request, Response response) throws BadRequestException, InternalServerException {
        Dog entity = readRequestBody(request);
        
        if (entity.getId() != Integer.valueOf(request.params("id"))) {
            throw new BadRequestException("Payload ID and URI ID do not match.");
        }
        
        EntityService.put(entity);
        
        response.body("");
        response.status(200);
        response.type("application/json");
        
        return response;
    }
}