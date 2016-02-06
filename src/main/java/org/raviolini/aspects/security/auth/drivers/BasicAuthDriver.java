package org.raviolini.aspects.security.auth.drivers;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

public class BasicAuthDriver extends AbstractAuthDriver {

    public BasicAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
    }

    @Override
    public String getName() {
        return "Basic";
    }
    
    @Override
    public String getChallenge() {
        String mask = "{0} realm=\"{1}\"";
        
        return MessageFormat.format(mask, getName(), getRealm());
    }
    
    @Override
    protected Boolean matchCredential(String requestMethod, String requestUri, String encodedCredential) {
        String decodedCredential = new String(Base64.getDecoder().decode(encodedCredential));
        StringTokenizer tokenizer = new StringTokenizer(decodedCredential, ":");
        
        if (tokenizer.countTokens() != 2) {
            return false;
        }
        
        String receivedUsername = tokenizer.nextToken();
        String receivedPassword = tokenizer.nextToken();
        
        return receivedUsername.equals(getUsername())
            && receivedPassword.equals(getPassword());
    }
}