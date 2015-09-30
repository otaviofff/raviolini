package org.raviolini.api;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import org.raviolini.api.adapter.AbstractRequestAdapter;
import org.raviolini.api.adapter.DeleteRequestAdapter;
import org.raviolini.api.adapter.GetRequestAdapter;
import org.raviolini.api.adapter.ListRequestAdapter;
import org.raviolini.api.adapter.PostRequestAdapter;
import org.raviolini.api.adapter.PutRequestAdapter;
import org.raviolini.api.exception.AbstractException;

public class FrontController {

    private static AbstractRequestAdapter adapter;
    
    public static void main(String[] args) {

        before((request, response) -> {
            FrontValidator.validateRequest(request);
        });
        
        before("/dog/:id", (request, response) -> {
            FrontValidator.validateUri(request);
        });
        
        post("/dog", (request, response) -> {
            adapter = new PostRequestAdapter();
            return adapter.handle(request, response).body();
        });
        
        get("/dog", (request, response) -> {
            adapter = new ListRequestAdapter();
            return adapter.handle(request, response).body();
        });
        
        get("/dog/:id", (request, response) -> {
            adapter = new GetRequestAdapter();
            return adapter.handle(request, response).body(); 
        });
        
        put("/dog/:id", (request, response) -> {
            adapter = new PutRequestAdapter();
            return adapter.handle(request, response).body();
        });
        
        delete("/dog/:id", (request, response) -> {
            adapter = new DeleteRequestAdapter();
            return adapter.handle(request, response).body();
        });
        
        exception(AbstractException.class, (e, request, response) -> {
            response.status(((AbstractException) e).getCode());
            response.body(e.getMessage());
            response.type("text/plain");
        });
    }
}