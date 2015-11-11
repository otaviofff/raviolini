package org.raviolini.api.exceptions;

public class InternalServerException extends AbstractException {

    private static final long serialVersionUID = 5899677486725875513L;

    public InternalServerException(Throwable cause) {
        super("Internal server error.", cause);
        this.code = 500;
    }
    
    public InternalServerException() {
        this(null);
    }
}