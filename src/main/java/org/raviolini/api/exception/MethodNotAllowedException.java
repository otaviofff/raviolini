package org.raviolini.api.exception;

public class MethodNotAllowedException extends AbstractException {

    private static final long serialVersionUID = -163468704023073277L;

    public MethodNotAllowedException() {
        super("Method not allowed.");
        this.code = 405;
    }
}