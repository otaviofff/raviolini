package org.raviolini.domain.dog;

import org.raviolini.api.exception.BadRequestException;

public class EntityValidator {
    
    public static void validate(Dog entity) throws BadRequestException {
        if (!entity.isValid()) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}