package org.raviolini.facade.exceptions;

public class WriteOperationException extends Exception {

    private static final long serialVersionUID = 3827898046871314871L;
    
    public WriteOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}