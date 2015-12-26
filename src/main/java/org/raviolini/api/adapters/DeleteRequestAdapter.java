package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.WriteOperationException;

import spark.Request;
import spark.Response;

public class DeleteRequestAdapter<T extends Entity> extends AbstractRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        Integer entityId = Integer.valueOf(request.params("id"));
        
        try {
            getService().delete(entityId, entityClass);
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