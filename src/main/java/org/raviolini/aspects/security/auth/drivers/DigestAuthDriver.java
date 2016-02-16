package org.raviolini.aspects.security.auth.drivers;

import java.text.MessageFormat;
import java.util.List;

import org.raviolini.aspects.security.auth.credentials.AbstractCredential;
import org.raviolini.aspects.security.auth.credentials.DigestCredential;

public class DigestAuthDriver extends AbstractAuthDriver {

    public DigestAuthDriver(DigestCredential expectedCredential, List<String> expectedMethods) {
        super(expectedCredential, expectedMethods);
    }
    
    @Override
    public String getName() {
        return "Digest";
    }
    
    @Override
    public String getChallenge() {
        DigestCredential expected = (DigestCredential) this.expectedCredential;
        
        String pattern = "{0} realm=\"{1}\", nonce=\"{2}\", opaque=\"{3}\"";
        
        return MessageFormat.format(
                pattern, 
                getName(), 
                expected.getRealm(), 
                expected.getNonce(), 
                expected.getOpaque()
                );
    }

    @Override
    protected AbstractCredential decodeCredential(String encodedCredential) {
        return DigestCredential.decode(encodedCredential);
    }

    @Override
    protected void scopeExpectedCredential(String requestMethod, String requestUri) {
        DigestCredential expected = (DigestCredential) this.expectedCredential;
        
        String response = DigestCredential.composeResponse(
                expected.getUsername(), 
                expected.getPassword(), 
                expected.getRealm(), 
                requestMethod, 
                requestUri
                );
        
        expected.setResponse(response);
    }
}