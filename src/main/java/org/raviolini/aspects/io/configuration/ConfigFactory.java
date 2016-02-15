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
     * However, this default can be overridden 
     *  through variable preferEnv.
     * 
     * @return AbstractConfigDriver[]
     */
    private static AbstractConfigDriver[] getDrivers(Boolean preferEnv) {
        if (preferEnv) {
            return new AbstractConfigDriver[] {
                new EnvConfigDriver(), 
                new FileConfigDriver()
            };
        }
        
        return new AbstractConfigDriver[] {
            new FileConfigDriver(), 
            new EnvConfigDriver()
        };
    }
    
    public static AbstractConfigDriver getDriver(Boolean preferEnv) {
        for (AbstractConfigDriver driver : getDrivers(preferEnv)) {
            if (driver.isActive()) {
                return driver;
            }
        }
        
        return null;
    }
 }