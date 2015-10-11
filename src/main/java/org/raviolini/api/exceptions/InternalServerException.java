package org.raviolini.api.exceptions;

public class InternalServerException extends AbstractException {

    private static final long serialVersionUID = 5899677486725875513L;

    public InternalServerException(String additionalInfo) {
        super("Internal server error. ".concat(additionalInfo));
        this.code = 500;
    }
}