package org.raviolini.api.adapters;

import org.raviolini.api.exceptions.AbstractException;
import org.raviolini.domain.Entity;

import spark.Request;
import spark.Response;

public abstract class AbstractRequestAdapter<T extends Entity> {
    
    public abstract Response handle(Request request, Response response, Class<T> entityClass) throws AbstractException;
}