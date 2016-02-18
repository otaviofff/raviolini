package org.raviolini.aspects.io.configuration;

import org.raviolini.aspects.io.configuration.drivers.AbstractConfigDriver;
import org.raviolini.aspects.io.configuration.drivers.EnvConfigDriver;
import org.raviolini.aspects.io.configuration.drivers.FileConfigDriver;

public class ConfigFactory {

    /***
     * Retrieve the list of all possible drivers. 
     * 
     * Note that FileConfigDriver 
     *  takes preference over the others, 
     *  given the local nature of its scope.
     * 
     * @return AbstractConfigDriver[]
     */
    private static AbstractConfigDriver[] getDrivers() {
        if (ConfigRegistry.getInstance().loaded()) {
            return new AbstractConfigDriver[] { 
                ConfigRegistry.getInstance().get() 
            };
        }
        
        return new AbstractConfigDriver[] {
            new FileConfigDriver(), 
            new EnvConfigDriver()
        };
    }
    
    public static AbstractConfigDriver getDriver() {
        for (AbstractConfigDriver driver : getDrivers()) {
            if (driver.isActive()) {
                return driver;
            }
        }
        
        return null;
    }
 }