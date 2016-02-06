package org.raviolini.api.filters;

import org.raviolini.api.exceptions.ForbiddenException;
import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.api.exceptions.UnauthorizedException;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.security.auth.AuthService;

import spark.FilterImpl;
import spark.Request;
import spark.Response;

public class AuthFilter extends FilterImpl {

    private AuthService auth;
    
    private AuthService getAuth() {
        if (auth == null) {
            auth = new AuthService();
        }
        
        return auth;
    }
    
    @Override
    public void handle(Request request, Response response) throws UnauthorizedException, ForbiddenException, InternalServerException {
        Boolean authenticated, authorized;
        String challenge;
        
        try {
            authenticated = getAuth().authenticate(request.requestMethod(), request.uri(), request.headers("Authorization"));
            authorized = getAuth().authorize(request.requestMethod());
            challenge = getAuth().challenge();
        } catch (UnloadableConfigException | InvalidPropertyException e) {
            throw new InternalServerException(e);
        }
        
        if (!authenticated) {
            response.header("WWW-Authenticate", challenge);
            throw new UnauthorizedException();
        }
        
        if (!authorized) {
            throw new ForbiddenException();
        }
    }
}