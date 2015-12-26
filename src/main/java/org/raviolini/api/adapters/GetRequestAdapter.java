package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.api.exceptions.NotFoundException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.ReadOperationException;

import spark.Request;
import spark.Response;

public class GetRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {
    
    @Override
    public Response handle(Request request, Response response, Class<T> entityCLass) throws InternalServerException, NotFoundException {
        String mediaType = request.headers("Accept");
        Integer entityId = Integer.valueOf(request.params("id"));
        
        T entity = null;
        
        try {
            entity = getService().get(entityId, entityCLass);
        } catch (ReadOperationException | HookExecutionException e) {
            throw new InternalServerException(e);
        } catch (Exception e) {
            throw new InternalServerException();
        }
        
        String body = serializeResponseBody(entity, mediaType);
        
        response.status(200);
        response.body(body);
        response.type(mediaType);

        return response;
    }
}