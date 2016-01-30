package org.raviolini.api.filters;

import org.raviolini.api.exceptions.NotFoundException;

import spark.FilterImpl;
import spark.Request;
import spark.Response;

public class RouteFilter extends FilterImpl {

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (response.raw().getStatus() == 0) {
            throw new NotFoundException();
        }
    }
}