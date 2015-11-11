package org.raviolini.api.exceptions;

public class BadRequestException extends AbstractException {

    private static final long serialVersionUID = -560967236938640467L;
    
    public BadRequestException(String additionalInfo) {
        super("Bad request. ".concat(additionalInfo));
        this.code = 400;
    }
    
    public BadRequestException(Throwable cause) {
        super("Bad request.", cause);
        this.code = 400;
    }
}