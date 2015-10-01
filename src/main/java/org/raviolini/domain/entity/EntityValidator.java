package org.raviolini.domain.entity;

import org.raviolini.api.exception.BadRequestException;

public class EntityValidator {
    
    public void validate(Entity entity) throws BadRequestException {
        if (!entity.isValid()) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}