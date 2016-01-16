package org.raviolini.aspects.io.serialization;

import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.JsonSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.XmlSerializationDriver;
import org.raviolini.domain.Entity;

public class SerializationFactory {
    
    public static <T extends Entity> AbstractSerializationDriver<T> getDriver(String mediaType) {
        AbstractSerializationDriver<T> driver;
        
        if (mediaType.startsWith("application/json")) {
            driver = new JsonSerializationDriver<>();
        } else if (mediaType.startsWith("application/xml")) {
            driver = new XmlSerializationDriver<>();
        } else {
            //Should never be executed.
            //Media type has already been checked by RequestValidator.
            driver = null;
        }
        
        return driver;
    }
}