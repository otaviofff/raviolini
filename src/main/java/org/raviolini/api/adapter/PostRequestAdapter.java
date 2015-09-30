package org.raviolini.api.adapter;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.dog.Dog;
import org.raviolini.domain.dog.EntityService;

import spark.Request;
import spark.Response;

public class PostRequestAdapter extends WriteRequestAdapter {

    @Override
    public Response handle(Request request, Response response) throws BadRequestException, InternalServerException {
        Dog entity = readRequestBody(request);
        EntityService.post(entity);
        
        response.body("");
        response.status(201);
        response.type("application/json");
        
        return response;
    }
}