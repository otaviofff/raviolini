package org.raviolini.aspects.io.serialization.exceptions;

public class SerializationException extends Exception {

    private static final long serialVersionUID = 4954683974406052277L;
    
    public SerializationException(Throwable cause) {
        super("Object serialization failed.", cause);
    }
}