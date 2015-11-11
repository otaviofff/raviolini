package org.raviolini.api.exceptions;

public abstract class AbstractException extends Exception {

    private static final long serialVersionUID = -7056725294381645766L;
    protected Integer code = 0;
    
    public AbstractException(String message) {
        super(message);
    }
    
    public AbstractException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public Integer getCode() {
        return code;
    }
}