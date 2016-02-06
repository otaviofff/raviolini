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
    
    public Boolean authenticate(String requestMethod, String requestUri, String encodedCredential) throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().authenticate(requestMethod, requestUri, encodedCredential);
    }
    
    public Boolean authorize(String requestMethod) throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().authorize(requestMethod);
    }
    
    public String challenge() throws UnloadableConfigException, InvalidPropertyException {
        return getDriver().getChallenge();
    }
}