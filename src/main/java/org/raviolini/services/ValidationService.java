package org.raviolini.services;

import org.raviolini.domain.Entity;

public class ValidationService {
    
    public void validate(Entity entity) throws IllegalArgumentException {
        if (!entity.isValid()) {
            throw new IllegalArgumentException();
        }
    }
}