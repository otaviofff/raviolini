package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

import org.raviolini.aspects.security.auth.Credential;

public abstract class AbstractAuthDriver {

    private String expectedUser;
    private String expectedPass;
    private List<String> authorizedMethods;
    
    public AbstractAuthDriver(String user, String pass, List<String> methods) {
        expectedUser = user;
        expectedPass = pass;
        authorizedMethods = methods;
    }
    
    public Boolean authenticate(String encodedCredential) {
        if (encodedCredential == null || !encodedCredential.startsWith(getName())) {
            return false;
        }
        
        Credential credential = parseCredential(encodedCredential.substring(getName().length()).trim());
        
        if (credential == null) {
            return false;
        }
        
        return expectedUser.equals(credential.getUsername()) &&
               expectedPass.equals(credential.getPassword());
    }
    
    public Boolean authorize(String method) {
        return method != null
                && authorizedMethods != null
                && authorizedMethods.contains(method);
    }
    
    protected abstract String getName();
    
    protected abstract Credential parseCredential(String encodedCredential);
}