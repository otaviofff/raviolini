package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.WriteOperationException;

import spark.Request;
import spark.Response;

public class PostRequestAdapter<T extends Entity> extends WriteRequestAdapter<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException, BadRequestException {
        T entity = unserializeRequestBody(request, entityClass);
        
        try {
            getService().post(entity, entityClass);
        } catch (WriteOperationException | HookExecutionException e) {
            throw new InternalServerException(e);
        } catch (Exception e) {
            throw new InternalServerException();
        }
        
        response.status(201);
        response.body("");
        response.type("text/plain");
        
        return response;
    }
}