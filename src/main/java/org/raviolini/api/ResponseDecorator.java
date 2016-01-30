package org.raviolini.api;

import org.raviolini.api.exceptions.AbstractException;

import spark.Response;

public class ResponseDecorator {
    
    public static void decorateFromException(Response response, AbstractException e) {
        response.status(e.getCode());
        response.body(AbstractException.chainNestedMessages(e));
        response.type("text/plain");
    }
}