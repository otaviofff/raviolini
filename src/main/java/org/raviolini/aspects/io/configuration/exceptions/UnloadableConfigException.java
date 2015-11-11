package org.raviolini.aspects.io.configuration.exceptions;

public class UnloadableConfigException extends Exception {

    private static final long serialVersionUID = 1988467257700841605L;
    
    public UnloadableConfigException(Throwable cause) {
        super("Config file could not be loaded.", cause);
    }
}