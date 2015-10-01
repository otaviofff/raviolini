package org.raviolini.api.adapter;

import org.raviolini.api.exception.AbstractException;
import org.raviolini.domain.entity.Entity;

import spark.Request;
import spark.Response;

public abstract class AbstractRequestAdapter<T extends Entity> {
    
    public abstract Response handle(Request request, Response response, Class<T> entityClass) throws AbstractException;
}