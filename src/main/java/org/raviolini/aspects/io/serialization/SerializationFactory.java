package org.raviolini.aspects.io.serialization;

import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.JsonSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.XmlSerializationDriver;
import org.raviolini.domain.Entity;

public class SerializationFactory {
    
    public static <T extends Entity> AbstractSerializationDriver<T> getDriver(String mediaType) {
        AbstractSerializationDriver<T> driver;
        
        switch (mediaType) {
            case "application/json":
                driver = new JsonSerializationDriver<T>();
                break;
            case "application/xml":
                driver = new XmlSerializationDriver<T>();
                break;
            default:
                //Should never be executed.
                //Media type has already been checked by RequestValidator.
                driver = null;
        }
        
        return driver;
    }
}