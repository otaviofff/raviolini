package org.raviolini.api.adapter;

import org.raviolini.api.exception.AbstractException;

import spark.Request;
import spark.Response;

public abstract class AbstractRequestAdapter {
    
    public abstract Response handle(Request request, Response response) throws AbstractException;
}