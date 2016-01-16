package org.raviolini.api;

import java.util.Arrays;
import java.util.List;

import org.raviolini.api.exceptions.AbstractException;
import org.raviolini.api.exceptions.BadRequestException;
import org.raviolini.api.exceptions.MethodNotAllowedException;
import org.raviolini.api.exceptions.NotAcceptableException;
import org.raviolini.api.exceptions.UnsupportedMediaTypeException;

import spark.Request;

public class RequestValidator {
    
    //TODO: Implement authentication.
    
    private static List<String> supportedMethods = Arrays.asList(
            "GET", 
            "POST", 
            "PUT", 
            "DELETE"
    );
    
    private static List<String> supportedMediaTypes = Arrays.asList(
            "application/json",
            "application/json; charset=utf-8", 
            "application/xml",
            "application/xml; charset=utf-8"
    );

    public static void validateRequest(Request request) throws AbstractException {
        validateMethod(request);
        validateHeaders(request);
        validateBody(request);
    }
    
    public static void validateUri(Request request) throws BadRequestException {
        try {
            Integer.valueOf(request.params("id"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("URI ID not an integer.");
        }
    }
    
    private static void validateMethod(Request request) throws MethodNotAllowedException {
        if (!supportedMethods.contains(request.requestMethod().toUpperCase())) {
            throw new MethodNotAllowedException();
        }
    }
    
    private static void validateHeaders(Request request) throws NotAcceptableException, UnsupportedMediaTypeException {
        switch (request.requestMethod()) {
            case "GET":
                if (request.headers("Accept") == null 
                        || !supportedMediaTypes.contains(request.headers("Accept").toLowerCase())) {
                    throw new NotAcceptableException();
                }
                break;
                
            case "PUT":
            case "POST":
                if (request.headers("Content-Type") == null 
                        || !supportedMediaTypes.contains(request.headers("Content-Type").toLowerCase())) {
                    throw new UnsupportedMediaTypeException();
                }
                break;
        }
    }
    
    private static void validateBody(Request request) throws BadRequestException {
        switch (request.requestMethod()) {
            case "GET":
            case "DELETE":
                if (!request.body().isEmpty()) {
                    throw new BadRequestException("Request body has to be empty.");
                }
                break;
                
            case "PUT":
            case "POST":
                if (request.body().isEmpty()) {
                    throw new BadRequestException("Request body cannot be empty.");
                }
                break;
        }
    }
}