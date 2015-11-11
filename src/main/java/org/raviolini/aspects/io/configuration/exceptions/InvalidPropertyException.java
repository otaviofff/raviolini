package org.raviolini.aspects.io.configuration.exceptions;

public class InvalidPropertyException extends Exception {

    private static final long serialVersionUID = -4122065374261667524L;
    
    public InvalidPropertyException(String propertyName, Throwable cause) {
        super("Invalid property set in config file: ".concat(propertyName).concat("."), cause);
    }
}