package org.raviolini.api.exception;

public class NotFoundException extends AbstractException {

    private static final long serialVersionUID = 994688501677727894L;

    public NotFoundException() {
        super("Not found.");
        this.code = 404;
    }
}