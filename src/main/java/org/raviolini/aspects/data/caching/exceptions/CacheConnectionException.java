package org.raviolini.aspects.data.caching.exceptions;

public class CacheConnectionException extends Exception {

    private static final long serialVersionUID = 3054455307455513792L;
    
    public CacheConnectionException(Throwable cause) {
        super("Cache connection failed.", cause);
    }
}
