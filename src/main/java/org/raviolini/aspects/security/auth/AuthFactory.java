package org.raviolini.aspects.security.auth;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.raviolini.aspects.io.configuration.ConfigService;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;
import org.raviolini.aspects.security.auth.drivers.AbstractAuthDriver;
import org.raviolini.aspects.security.auth.drivers.BasicAuthDriver;
import org.raviolini.aspects.security.auth.drivers.DigestAuthDriver;
import org.raviolini.aspects.security.auth.drivers.NullAuthDriver;

public class AuthFactory {

    private static ConfigService getConfig() {
        return new ConfigService();
    }
    
    private static String getConfigNamespace() {
        return "raviolini.auth";
    }
    
    private static String[] getConfigKeys() {
        return new String[] {"driver", "user", "pass", "methods"};
    }
    
    public static AbstractAuthDriver getDriver() throws UnloadableConfigException, InvalidPropertyException {
        Map<String, String> map = getConfig().read(getConfigNamespace(), getConfigKeys());
        
        String driver  = map.get("driver");
        String user    = map.get("user");
        String pass    = map.get("pass");
        String methods = map.get("methods");
        
        return instantiateDriver(driver, user, pass, explodeAuthorizedMethods(methods));
    }
    
    private static List<String> explodeAuthorizedMethods(String methods) {
        if (methods == null) {
            return null;
        }
        
        StringTokenizer tokenizer = new StringTokenizer(methods, ",");
        Integer count = tokenizer.countTokens();
        String[] list = new String[count];
        
        for (int i = 0; i < count; i ++) {
            list[i] = tokenizer.nextToken().trim();
        }
        
        return Arrays.asList(list);
    }
    
    private static AbstractAuthDriver instantiateDriver(String driver, String user, String pass, List<String> methods) throws InvalidPropertyException {
        if (driver == null) {
            driver = "invalid";
        }
        
        switch (driver.toLowerCase()) {
            case "basic":
                return new BasicAuthDriver(user, pass, methods);
            case "digest":
                return new DigestAuthDriver(user, pass, methods);
            case "null":
                return new NullAuthDriver(user, pass, methods);
            default:
                throw new InvalidPropertyException(getConfigNamespace().concat(".driver"), null);
        }
    }
}