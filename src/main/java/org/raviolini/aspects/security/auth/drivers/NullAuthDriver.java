package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

import org.raviolini.aspects.security.auth.Credential;

public class NullAuthDriver extends AbstractAuthDriver {

    public NullAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
    }

    @Override
    public String getChallenge() {
        return null;
    }

    @Override
    protected String getName() {
        return null;
    }
    
    @Override
    protected Credential parseCredential(String encodedCredential) {
        return null;
    }
    
    @Override
    public Boolean authenticate(String envodedCredential) {
        return true;
    }
}