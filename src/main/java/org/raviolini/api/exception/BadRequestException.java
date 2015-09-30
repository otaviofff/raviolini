package org.raviolini.api.exception;

public class BadRequestException extends AbstractException {

    private static final long serialVersionUID = -560967236938640467L;
    
    public BadRequestException(String additionalInfo) {
        super("Bad request. ".concat(additionalInfo));
        this.code = 400;
    }
}