package org.raviolini.aspects.data.validation;

import org.raviolini.aspects.data.validation.exceptions.InvalidEntityException;
import org.raviolini.domain.Entity;

public class ValidationService {
    
    public boolean validate(Entity entity) throws InvalidEntityException {
        if (!entity.isValid()) {
            throw new InvalidEntityException();
        }
        
        return true;
    }
}