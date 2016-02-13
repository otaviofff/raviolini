package org.raviolini.api.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.facade.exceptions.HookExecutionException;
import org.raviolini.facade.exceptions.ReadOperationException;

import spark.Request;
import spark.Response;

public class ListRequestAdapter<T extends Entity> extends ReadRequestAdaptar<T> {

    @Override
    public Response handle(Request request, Response response, Class<T> entityClass) throws InternalServerException {
        List<T> list = null;
        Long itemsReturned, itemsStored = (long) 0;
        HashMap<String, String> params = getRequestParams(request);
        
        try {
            list = getService().get(params, entityClass);
            itemsReturned = (long) list.size();
            
            if (itemsReturned > 0) {
                itemsStored = getService().count(params, entityClass);
            }
        } catch (ReadOperationException | HookExecutionException e) {
            throw new InternalServerException(e);
        } catch (Exception e) {
            throw new InternalServerException();
        }

        String type = request.headers("Accept");
        String body = serializeResponseBody(list, type);
        
        response.status(200);
        response.body(body);
        response.type(type);
        response.header("X-Items-Returned", String.valueOf(itemsReturned));
        response.header("X-Items-Stored", String.valueOf(itemsStored));
        
        return response;
    }
    
    private HashMap<String, String> getRequestParams(Request request) {
        HashMap<String, String> map = new HashMap<>();
        Set<String> set = request.queryParams();
        
        for (String key : set) {
            String value = request.queryParams(key);
            map.put(key, value);
        }
        
        return map;
    }
}