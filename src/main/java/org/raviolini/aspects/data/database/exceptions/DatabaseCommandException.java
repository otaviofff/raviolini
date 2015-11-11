package org.raviolini.aspects.data.database.exceptions;

import java.sql.SQLException;

public class DatabaseCommandException extends SQLException {

    private static final long serialVersionUID = -6317979284887898340L;
    
    public DatabaseCommandException(String commandName, Throwable cause) {
        super("Database command failed: ".concat(commandName).concat("."), cause);
    }
}