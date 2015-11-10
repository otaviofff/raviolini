package org.raviolini.api;

import org.raviolini.api.exceptions.AbstractException;

import spark.Response;

public class ResponseDecorator {
    
    public static void decorateFromException(Response response, AbstractException e) {
        response.status(e.getCode());
        response.body(composeBody(e));
        response.type("text/plain");
    }
    
    private static String composeBody(Throwable e) {
        if (e != null && e.getClass().getName().startsWith("org.raviolini")) {
            return e.getMessage().concat(" ").concat(composeBody(e.getCause()));
        }
        
        return "";
    }
}