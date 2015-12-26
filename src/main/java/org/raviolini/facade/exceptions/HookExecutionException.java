package org.raviolini.facade.exceptions;

public class HookExecutionException extends Exception {

    private static final long serialVersionUID = -4752223454348427271L;
    
    public HookExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}