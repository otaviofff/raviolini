package org.raviolini.api;

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
import org.raviolini.aspects.io.logging.LogService;
import org.raviolini.domain.Entity;
import org.raviolini.facade.EntityService;

public class RequestRouter<T extends Entity> {

    private LogService logger;
    private EntityService<T> service;
    private AbstractRequestAdapter<T> adapter;
    
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
            prepareAdapter();
            return adapter.handle(request, response, entityClass).body();
        });
        
        get(entityListUri, (request, response) -> {
            adapter = new ListRequestAdapter<>();
            prepareAdapter();
            return adapter.handle(request, response, entityClass).body();
        });
        
        get(entityUri, (request, response) -> {
            adapter = new GetRequestAdapter<>();
            prepareAdapter();
            return adapter.handle(request, response, entityClass).body(); 
        });
        
        put(entityUri, (request, response) -> {
            adapter = new PutRequestAdapter<>();
            prepareAdapter();
            return adapter.handle(request, response, entityClass).body();
        });
        
        delete(entityUri, (request, response) -> {
            adapter = new DeleteRequestAdapter<>();
            prepareAdapter();
            return adapter.handle(request, response, entityClass).body();
        });
        
        exception(AbstractException.class, (e, request, response) -> {
            getLogger().logException(e, true);
            ResponseDecorator.decorateFromException(response, (AbstractException) e);
        });
    }
    
    private void prepareAdapter() {
        if (adapter != null && service != null) {
            adapter.setService(service);
        }
    }
}