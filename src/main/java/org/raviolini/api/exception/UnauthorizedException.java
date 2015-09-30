package org.raviolini.api.exception;

public class UnauthorizedException extends AbstractException {

    private static final long serialVersionUID = -2904653147666227777L;

    public UnauthorizedException() {
        super("Unauthorized.");
        this.code = 401;
    }
}