package org.raviolini.facade.exceptions;

public class ReadOperationException extends Exception {

    private static final long serialVersionUID = -7787107731793511186L;
    
    public ReadOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}