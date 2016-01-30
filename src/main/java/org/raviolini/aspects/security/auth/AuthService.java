package org.raviolini.aspects.security.auth;

import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.security.auth.drivers.AbstractAuthDriver;

public class AuthService {

    private AbstractAuthDriver driver;
    
    private AbstractAuthDriver getDriver() throws UnloadableConfigException, InvalidPropertyException {
        if (driver == null) {
            driver = AuthFactory.getDriver();
        }
        
        return driver;
    }
    
    public Boolean authenticate(String encodedCredential) throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().authenticate(encodedCredential);
    }
    
    public Boolean authorize(String method) throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().authorize(method);
    }
    
    public String challenge() throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().getChallenge();
    }
}