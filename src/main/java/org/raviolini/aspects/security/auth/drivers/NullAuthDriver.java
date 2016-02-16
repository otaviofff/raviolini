package org.raviolini.aspects.security.auth.drivers;

import org.raviolini.aspects.security.auth.credentials.AbstractCredential;

public class NullAuthDriver extends AbstractAuthDriver {

    public NullAuthDriver() {
        super(null, null);
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
    protected AbstractCredential decodeCredential(String encodedCredential) {
        return null;
    }

    @Override
    protected void scopeExpectedCredential(String requestMethod, String requestUri) {
        
    }
}