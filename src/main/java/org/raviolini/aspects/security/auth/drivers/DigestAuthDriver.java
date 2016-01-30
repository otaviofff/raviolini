package org.raviolini.aspects.security.auth.drivers;

import java.util.List;

import org.raviolini.aspects.security.auth.Credential;

public class DigestAuthDriver extends AbstractAuthDriver {

    public DigestAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
    }

    @Override
    protected String getName() {
        return "Digest";
    }
    
    @Override
    protected Credential parseCredential(String encodedCredential) {
        // TODO: Implement credential parsing for Digest authentication.
        return null;
    }
}