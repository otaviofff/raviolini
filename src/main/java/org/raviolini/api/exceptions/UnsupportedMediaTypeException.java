package org.raviolini.api.exceptions;

public class UnsupportedMediaTypeException extends AbstractException {

    private static final long serialVersionUID = -4399056615955863666L;

    public UnsupportedMediaTypeException() {
        super("Unsupported media type.");
        this.code = 415;
    }
}