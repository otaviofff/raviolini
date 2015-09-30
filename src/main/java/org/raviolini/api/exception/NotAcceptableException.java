package org.raviolini.api.exception;

public class NotAcceptableException extends AbstractException {

    private static final long serialVersionUID = 8308083403927394439L;

    public NotAcceptableException() {
        super("Not acceptable.");
        this.code = 406;
    }
}