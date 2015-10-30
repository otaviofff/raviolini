package org.raviolini.aspects.io.serialization;

import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.JsonSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.XmlSerializationDriver;
import org.raviolini.domain.Entity;

public class SerializationFactory {
    
    public static <T extends Entity> AbstractSerializationDriver<T> getDriver(String mediaType) throws InvalidPropertyException {
        switch (mediaType) {
            case "application/json":
                return new JsonSerializationDriver<T>();
            case "application/xml":
                return new XmlSerializationDriver<T>();
            default:
                throw new InvalidPropertyException();
        }
    }
}