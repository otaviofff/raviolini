package org.raviolini.api.adapters;

import java.util.List;

import org.raviolini.api.exceptions.InternalServerException;
import org.raviolini.api.exceptions.NotFoundException;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.domain.Entity;

public abstract class ReadRequestAdaptar<T extends Entity> extends AbstractRequestAdapter<T> {

    protected String serializeResponseBody(T entity, String mediaType) throws InternalServerException, NotFoundException {
        if (entity == null) {
            throw new NotFoundException();
        }
        
        try {
            return getSerializer(mediaType).serialize(entity);
        } catch (SerializationException e) {
            throw new InternalServerException(e);
        }
    }
    
    protected String serializeResponseBody(List<T> list, String mediaType) throws InternalServerException {
        try {
            return getSerializer(mediaType).serialize(list);
        } catch (SerializationException e) {
            throw new InternalServerException(e);
        }
    }
}