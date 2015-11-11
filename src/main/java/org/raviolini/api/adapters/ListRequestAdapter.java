package org.raviolini.api.adapters;

import java.util.List;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.ReadOperationException;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        String mediaType = request.headers("Accept");
        
        List<T> list = null;
        
        try {
            list = getService().get(entityClass);
        } catch (ReadOperationException e) {
            throw new InternalServerException(e);
        } catch (Exception e) {
            throw new InternalServerException();
        }
        
        String body = serializeResponseBody(list, mediaType);
        
        response.status(200);
        response.body(body);
        response.type(mediaType);
        
        return response;
    }
}