package org.raviolini.aspects.security.auth;

import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.raviolini.aspects.security.crypt.CryptService;

public class AuthClient {
    
    private String username;
    private String password;
    private String method;
    private String uri;
    
    public AuthClient(String username, String password, String method, String uri) {
        this.username = username;
        this.password = password;
        this.method = method;
        this.uri = uri;
    }
    
    public String encodeCredential(String challenge) {
        if (challenge.startsWith("Digest")) {
            challenge = challenge.substring("Digest".length()).trim();
        }
        
        StringTokenizer tokenizer = new StringTokenizer(challenge, ",");

        Pattern pattern = Pattern.compile("(.*)=\"([^\"]*)\"");
        Matcher match;
        
        String token, field, value;
        String realm = "";
        String nonce = "";
        String opaque = "";
        
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken().trim();
            match = pattern.matcher(token);
            
            if (match.matches() == false || match.groupCount() != 2) {
                return null;
            }
            
            field = match.group(1).trim().toLowerCase();
            value = match.group(2).trim();
            
            switch (field) {
                case "realm":
                    realm = value;
                    break;
                case "nonce":
                    nonce = value;
                    break;
                case "opaque":
                    opaque = value;
                    break;
            }
        }
        
        String response = composeResponse(username, password, realm, nonce, method, uri);
        String encoded = composeCredential(username, realm, nonce, opaque, response);
        
        return encoded;
    }
    
    private String composeResponse(String username, String password, String realm, String nonce, String requestMethod, String requestUri) {
        CryptService crypt = new CryptService();
        
        String mask1 = "{0}:{1}:{2}";
        String mask2 = "{0}:{1}";
        String a1 = crypt.hash(MessageFormat.format(mask1, username, realm, password));
        String a2 = crypt.hash(MessageFormat.format(mask2, requestMethod, requestUri));
        String response = MessageFormat.format(mask1, a1, nonce, a2);
        
        return crypt.hash(response);
    }
    
    private String composeCredential(String username, String realm, String nonce, String opaque, String response) { 
        String mask = "Digest realm=\"{0}\",username=\"{1}\",nonce=\"{2}\",opaque=\"{3}\",response=\"{4}\"";
        String credential = MessageFormat.format(mask, realm, username, nonce, opaque, response);
        
        return credential;
    }
}