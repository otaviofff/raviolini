package org.raviolini.aspects.security.auth.drivers;

import java.text.MessageFormat;
import java.util.List;

import org.raviolini.aspects.security.auth.credentials.AbstractCredential;
import org.raviolini.aspects.security.auth.credentials.BasicCredential;

public class BasicAuthDriver extends AbstractAuthDriver {

    public BasicAuthDriver(BasicCredential expectedCredential, List<String> expectedMethods) {
        super(expectedCredential, expectedMethods);
    }

    @Override
    public String getName() {
        return "Basic";
    }
    
    @Override
    public String getChallenge() {
        BasicCredential expected = (BasicCredential) this.expectedCredential;
        
        String pattern = "{0} realm=\"{1}\"";
        
        return MessageFormat.format(
                pattern, 
                getName(), 
                expected.getRealm()
                );
    }
    
    @Override
    protected AbstractCredential decodeCredential(String encodedCredential) {
        return BasicCredential.decode(encodedCredential);
    }

    @Override
    protected void scopeExpectedCredential(String requestMethod, String requestUri) {
        // Basic authentication isn't scoped in terms of HTTP method or URI.
    }
}