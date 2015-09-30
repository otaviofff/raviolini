package org.raviolini.api.adapter;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.dog.EntityService;

import spark.Request;
import spark.Response;

public class DeleteRequestAdapter extends AbstractRequestAdapter {

    @Override
    public Response handle(Request request, Response response) throws InternalServerException {
        Integer id = Integer.valueOf(request.params("id"));
        EntityService.delete(id);
        
        response.body("");
        response.status(200);
        response.type("application/json");
        
        return response;
    }
}