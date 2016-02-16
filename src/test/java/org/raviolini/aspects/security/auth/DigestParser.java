package org.raviolini.aspects.security.auth;

import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.raviolini.aspects.security.crypt.CryptService;

public class DigestParser {
    
    public static String composeCredentialFromChallenge(String challenge, String username, String password, String method, String uri) {
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
        
        String response = composeResponse(realm, username, password, method, uri, nonce);
        String credential = composeCredential(realm, username, nonce, opaque, response);
        
        return credential;
    }
    
    private static String composeResponse(String realm, String username, String password, String method, String uri, String nonce) {
        CryptService crypt = new CryptService();
        
        String mask1, mask2, a1, a2, response;
        
        mask1 = "{0}:{1}:{2}";
        mask2 = "{0}:{1}";
        a1 = crypt.hash(MessageFormat.format(mask1, username, realm, password));
        a2 = crypt.hash(MessageFormat.format(mask2, method, uri));
        response = crypt.hash(MessageFormat.format(mask1, a1, nonce, a2));
        
        return response;
    }
    
    private static String composeCredential(String realm, String username, String nonce, String opaque, String response) { 
        String mask = "Digest realm=\"{0}\",username=\"{1}\",nonce=\"{2}\",opaque=\"{3}\",response=\"{4}\"";
        String credential = MessageFormat.format(mask, realm, username, nonce, opaque, response);
        
        return credential;
    }
}