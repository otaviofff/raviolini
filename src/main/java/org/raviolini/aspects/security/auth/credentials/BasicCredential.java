package org.raviolini.aspects.security.auth.credentials;

import java.util.Base64;
import java.util.StringTokenizer;

public class BasicCredential extends AbstractCredential {

    private String realm;
    
    public BasicCredential(String username, String password, String realm) {
        super(username, password);
        this.realm = realm;
    }
    
    public String getRealm() {
        return realm;
    }
    
    @Override
    public boolean equals(Object obj) {
        try {
            BasicCredential tested = (BasicCredential) obj;
            
            // Realm isn't sent by the API client.
            // So not tested here.
            return super.equals(tested);
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    public static BasicCredential decode(String encodedCredential) {
        String decodedCredential = new String(Base64.getDecoder().decode(encodedCredential));
        StringTokenizer tokenizer = new StringTokenizer(decodedCredential, ":");
        
        if (tokenizer.countTokens() != 2) {
            return null;
        }
        
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        
        return new BasicCredential(username, password, null);
    }
}