package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

public class NullAuthDriver extends AbstractAuthDriver {

    public NullAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public String getChallenge() {
        return null;
    }
    
    @Override
    public Boolean authenticate(String requestMethod, String requestUri, String encodedCredential) {
        return true;
    }

    @Override
    protected Boolean matchCredential(String requestMethod, String requestUri, String encodedCredential) {
        return true;
    }
}