package org.raviolini.aspects.io.serialization.drivers;

import org.raviolini.domain.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializationDriver<T extends Entity> extends AbstractSerializationDriver<T> {

    @Override
    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}