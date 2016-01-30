package org.raviolini.api.exceptions;

public class ForbiddenException extends AbstractException {

    private static final long serialVersionUID = -7702108966601017972L;

    public ForbiddenException() {
        super("Forbidden.");
        this.code = 403;
    }
}