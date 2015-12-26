package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.WriteOperationException;

import spark.Request;
import spark.Response;

public class PutRequestAdapter<T extends Entity> extends WriteRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException, BadRequestException {
        T entity = unserializeRequestBody(request, entityClass);
        
        if (entity.getId() != Integer.valueOf(request.params("id"))) {
            throw new BadRequestException("Payload ID and URI ID do not match.");
        }
        
        try {
            getService().put(entity, entityClass);
        } catch (WriteOperationException | HookExecutionException e) {
            throw new InternalServerException(e);
        } catch (Exception e) {
            throw new InternalServerException();
        }
        
        response.status(200);
        response.body("");
        response.type("text/plain");
        
        return response;
    }
}