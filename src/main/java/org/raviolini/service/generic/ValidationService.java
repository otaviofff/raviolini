package org.raviolini.service.generic;

import org.raviolini.api.exception.BadRequestException;
import org.raviolini.domain.Entity;

public class ValidationService {
    
    public void validate(Entity entity) throws BadRequestException {
        if (!entity.isValid()) {
            throw new BadRequestException("Invalid payload given.");
        }
    }
}