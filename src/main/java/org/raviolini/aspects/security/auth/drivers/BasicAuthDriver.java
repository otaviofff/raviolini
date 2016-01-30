package org.raviolini.aspects.security.auth.drivers;

import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import org.raviolini.aspects.security.auth.Credential;

public class BasicAuthDriver extends AbstractAuthDriver {

    public BasicAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
    }

    @Override
    public String getChallenge() {
        return "Basic realm=\"raviolini.auth\"";
    }
    
    @Override
    protected String getName() {
        return "Basic";
    }
    
    @Override
    protected Credential parseCredential(String encodedCredential) {
        String decodedCredential = new String(Base64.getDecoder().decode(encodedCredential));
        StringTokenizer tokenizer = new StringTokenizer(decodedCredential, ":");
        
        if (tokenizer.countTokens() != 2) {
            return null;
        }
        
        return new Credential(tokenizer.nextToken(), tokenizer.nextToken());
    }
}