package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

import org.raviolini.aspects.security.auth.credentials.AbstractCredential;

public abstract class AbstractAuthDriver {

    protected AbstractCredential expectedCredential;
    protected List<String> expectedMethods;
    
    public AbstractAuthDriver(AbstractCredential expectedCredential, List<String> expectedMethods) {
        this.expectedCredential = expectedCredential;
        this.expectedMethods = expectedMethods;
    }
    
    public Boolean authenticate(String requestMethod, String requestUri, String encodedCredential) {
        AbstractCredential decodedCredential;
        
        if (encodedCredential == null || !encodedCredential.startsWith(getName())) {
            return false;
        }
        
        scopeExpectedCredential(requestMethod, requestUri);
        
        encodedCredential = encodedCredential.substring(getName().length()).trim();
        decodedCredential = decodeCredential(encodedCredential);
        
        return expectedCredential.equals(decodedCredential);
    }
    
    public Boolean authorize(String requestMethod) {
        return requestMethod != null
            && expectedMethods != null
            && expectedMethods.contains(requestMethod);
    }

    public abstract String getName();
    
    public abstract String getChallenge();
    
    protected abstract AbstractCredential decodeCredential(String encodedCredential);
    
    protected abstract void scopeExpectedCredential(String requestMethod, String requestUri);
}