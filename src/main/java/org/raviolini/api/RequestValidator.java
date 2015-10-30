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
    
    private static List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE");
    private static List<String> supportedMediaTypes = Arrays.asList("application/json", "application/xml");

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
        if (!allowedMethods.contains(request.requestMethod())) {
            throw new MethodNotAllowedException();
        }
    }
    
    private static void validateHeaders(Request request) throws NotAcceptableException, UnsupportedMediaTypeException {
        switch (request.requestMethod()) {
            case "GET":
                if (request.headers("Accept") == null || !request.headers("Accept").equals(supportedMediaTypes)) {
                    throw new NotAcceptableException();
                }
                break;
                
            case "PUT":
            case "POST":
                if (request.headers("Content-Type") == null || !request.headers("Content-Type").equals(supportedMediaTypes)) {
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