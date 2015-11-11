package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.AbstractException;
import org.raviolini.aspects.io.serialization.SerializationService;
import org.raviolini.domain.Entity;
import org.raviolini.facade.EntityService;

import spark.Request;
import spark.Response;

public abstract class AbstractRequestAdapter<T extends Entity> {
    
    private SerializationService<T> serializer;
    private EntityService<T> service;
    
    protected SerializationService<T> getSerializer(String mediaType) {
        if (serializer == null) {
            serializer = new SerializationService<>(mediaType);
        }
        
        return serializer;
    }
    
    protected EntityService<T> getService() {
        if (service == null) {
            service = new EntityService<>();
        }
        
        return service;
    }
    
    public abstract Response handle(Request request, Response response, Class<T> entityClass) throws AbstractException;
}