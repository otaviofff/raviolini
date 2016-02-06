package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

public abstract class AbstractAuthDriver {

    private String expectedUser;
    private String expectedPass;
    private String expectedRealm;
    private List<String> expectedMethods;
    
    public AbstractAuthDriver(String user, String pass, List<String> methods) {
        expectedUser = user;
        expectedPass = pass;
        expectedRealm = "Impenetrable";
        expectedMethods = methods;
    }
    
    public abstract String getName();
    
    public abstract String getChallenge();
    
    protected String getUsername() {
        return expectedUser;
    }
    
    protected String getPassword() {
        return expectedPass;
    }
    
    protected String getRealm() {
        return expectedRealm;
    }
    
    public Boolean authenticate(String requestMethod, String requestUri, String encodedCredential) {
        if (encodedCredential == null || !encodedCredential.startsWith(getName())) {
            return false;
        }
        
        encodedCredential = encodedCredential.substring(getName().length()).trim();
        
        return matchCredential(requestMethod, requestUri, encodedCredential);
    }
    
    public Boolean authorize(String requestMethod) {
        return requestMethod != null
            && expectedMethods != null
            && expectedMethods.contains(requestMethod);
    }
    
    protected abstract Boolean matchCredential(String requestMethod, String requestUrl, String encodedCredential);
}