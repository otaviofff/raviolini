package org.raviolini.api;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.raviolini.api.adapters.AbstractRequestAdapter;
import org.raviolini.api.adapters.DeleteRequestAdapter;
import org.raviolini.api.adapters.GetRequestAdapter;
import org.raviolini.api.adapters.ListRequestAdapter;
import org.raviolini.api.adapters.PostRequestAdapter;
import org.raviolini.api.adapters.PutRequestAdapter;
import org.raviolini.api.exceptions.AbstractException;
import org.raviolini.api.filters.AuthFilter;
import org.raviolini.api.filters.RouteFilter;
import org.raviolini.aspects.io.logging.LogService;
import org.raviolini.domain.Entity;
import org.raviolini.facade.EntityService;

import spark.Request;
import spark.Response;

public class RequestRouter<T extends Entity> {

    private LogService logger;
    private EntityService<T> service;
    
    private LogService getLogger() {
        if (logger == null) {
            logger = new LogService();
        }
        
        return logger;
    }
    
    public void override(EntityService<T> service) {
        this.service = service;
    }
    
    public void route(Class<T> entityClass) {
        before(new AuthFilter());
        
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
            return handle(entityClass, request, response, new PostRequestAdapter<>());
        });
        
        get(entityListUri, (request, response) -> {
            return handle(entityClass, request, response, new ListRequestAdapter<>());
        });
        
        get(entityUri, (request, response) -> {
            return handle(entityClass, request, response, new GetRequestAdapter<>()); 
        });
        
        put(entityUri, (request, response) -> {
            return handle(entityClass, request, response, new PutRequestAdapter<>());
        });
        
        delete(entityUri, (request, response) -> {
            return handle(entityClass, request, response, new DeleteRequestAdapter<>());
        });
        
        after(new RouteFilter());
        
        exception(AbstractException.class, (e, request, response) -> {
            getLogger().logException(e, true);
            ResponseDecorator.decorateFromException(response, (AbstractException) e);
        });
    }
    
    private String handle(Class<T> entityClass, Request request, Response response, AbstractRequestAdapter<T> adapter) throws AbstractException {
        if (service != null) {
            adapter.setService(service);
        }
        
        return adapter.handle(request, response, entityClass).body();
    }
}