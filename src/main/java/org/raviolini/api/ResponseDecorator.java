package org.raviolini.api;

import org.raviolini.api.exceptions.AbstractException;

import spark.Response;

public class ResponseDecorator {
    
    public static void decorateFromException(Response response, Throwable e) {
        response.status(composeStatus(e));
        response.body(composeBody(e));
        response.type(composeType());
    }
    
    private static Integer composeStatus(Throwable e) {
        return ((AbstractException) e).getCode();
    }
    
    private static String composeBody(Throwable e) {
        if (e.getClass().getName().startsWith("org.raviolini")) {
            return e.getMessage().concat(" ").concat(composeBody(e.getCause()));
        }
        
        return "";
    }
    
    private static String composeType() {
        return "text/plain";
    }
}