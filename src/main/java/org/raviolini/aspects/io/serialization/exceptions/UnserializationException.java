package org.raviolini.aspects.io.serialization.exceptions;

public class UnserializationException extends Exception {

    private static final long serialVersionUID = 1699411506373760634L;
    
    public UnserializationException(Throwable cause) {
        super("Object unserialization failed.", cause);
    }
}