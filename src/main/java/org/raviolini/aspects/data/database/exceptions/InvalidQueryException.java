package org.raviolini.aspects.data.database.exceptions;

import java.sql.SQLException;

public class InvalidQueryException extends SQLException {

    private static final long serialVersionUID = 2211141832707455636L;

    public InvalidQueryException(String additionalInfo, Throwable cause) {
        super("Invalid query param (".concat(additionalInfo).concat(")."), cause);
    }
    
    public InvalidQueryException(Throwable cause) {
        super("Invalid query param.", cause);
    }
}