package org.raviolini.aspects.io.serialization;

import java.io.IOException;

import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.io.serialization.drivers.JsonSerializationDriver;
import org.raviolini.aspects.io.serialization.drivers.AbstractSerializationDriver;
import org.raviolini.domain.Entity;

public class SerializationFactory {
    
    public static <T extends Entity> AbstractSerializationDriver<T> getDriver() throws UnloadableConfigException, InvalidPropertyException {
        String driver;
        ConfigService config = new ConfigService();
        
        try {
            driver = config.read("raviolini.serializer", "driver");
        } catch (IOException e ) {
            throw new UnloadableConfigException();
        }
        
        switch (driver) {
            case "json":
                return new JsonSerializationDriver<T>();
            default:
                throw new InvalidPropertyException();
        }
    }
}