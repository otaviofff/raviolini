package org.raviolini.api;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.raviolini.api.adapter.AbstractRequestAdapter;
import org.raviolini.api.adapter.DeleteRequestAdapter;
import org.raviolini.api.adapter.GetRequestAdapter;
import org.raviolini.api.adapter.ListRequestAdapter;
import org.raviolini.api.adapter.PostRequestAdapter;
import org.raviolini.api.adapter.PutRequestAdapter;
import org.raviolini.api.exception.AbstractException;
import org.raviolini.domain.entity.Entity;
import org.raviolini.service.LoggingService;

public class RequestRouter<T extends Entity> {

    private AbstractRequestAdapter<T> adapter;  
    
    public void route(Class<T> entityClass) {
        
        String entityName = entityClass.getSimpleName().toLowerCase();
        String entityListUri = ("/").concat(entityName);
        String entityUri = entityListUri.concat("/:id");
        
        before((request, response) -> {
            RequestValidator.validateRequest(request);
        });
        
        before(entityUri, (request, response) -> {
            RequestValidator.validateUri(request);
        });
        
        post(entityListUri, (request, response) -> {
            adapter = new PostRequestAdapter<>();
            return adapter.handle(request, response, entityClass).body();
        });
        
        get(entityListUri, (request, response) -> {
            adapter = new ListRequestAdapter<>();
            return adapter.handle(request, response, entityClass).body();
        });
        
        get(entityUri, (request, response) -> {
            adapter = new GetRequestAdapter<>();
            return adapter.handle(request, response, entityClass).body(); 
        });
        
        put(entityUri, (request, response) -> {
            adapter = new PutRequestAdapter<>();
            return adapter.handle(request, response, entityClass).body();
        });
        
        delete(entityUri, (request, response) -> {
            adapter = new DeleteRequestAdapter<>();
            return adapter.handle(request, response, entityClass).body();
        });
        
        exception(AbstractException.class, (e, request, response) -> {
            logException(e);
            response.status(((AbstractException) e).getCode());
            response.body(e.getMessage());
            response.type("text/plain");
        });
    }
    
    private static void logException(Exception e) {
        LoggingService logger = new LoggingService();
        logger.logException(e, false);
    }
}