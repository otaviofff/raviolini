package org.raviolini.aspects.security.auth.credentials;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.raviolini.aspects.security.crypt.CryptService;

public class DigestCredential extends BasicCredential {

    private String nonce;
    private String opaque;
    private String response;
    
    public DigestCredential(String username, String password, String realm, String nonce, String opaque, String response) {
        super(username, password, realm);
        this.nonce = nonce;
        this.opaque = opaque;
        this.response = response;
    }
    
    public DigestCredential(String username, String password, String realm, String nonce, String opaque) {
        this(username, password, realm, nonce, opaque, null);
    }
    
    public String getNonce() {
        return nonce;
    }
    
    public String getOpaque() {
        return opaque;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    @Override
    public boolean equals(Object obj) {
        try {
            DigestCredential tested = (DigestCredential) obj;
            
            return getUsername().equals(tested.getUsername()) &&
                   getRealm().equals(tested.getRealm()) &&
                   getNonce().equals(tested.getNonce()) &&
                   getOpaque().equals(tested.getOpaque()) &&
                   getResponse().equals(tested.getResponse());
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    public static DigestCredential decode(String encodedCredential) {
        StringTokenizer tokenizer = new StringTokenizer(encodedCredential, ",");
    
        Pattern pattern = Pattern.compile("(.*)=\"([^\"]*)\"");
        Matcher match;
        
        String token, field, value;
        String username = "";
        String realm = "";
        String nonce = "";
        String opaque = "";
        String response = "";
        
        if (tokenizer.countTokens() < 5) {
            return null;
        }
        
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken().trim();
            match = pattern.matcher(token);
            
            if (match.matches() == false || match.groupCount() != 2) {
                return null;
            }
            
            field = match.group(1).trim().toLowerCase();
            value = match.group(2).trim();
            
            switch (field) {
                case "username":
                    username = value;
                    break;
                case "realm":
                    realm = value;
                    break;
                case "nonce":
                    nonce = value;
                    break;
                case "opaque":
                    opaque = value;
                    break;
                case "response":
                    response = value;
                    break;
            }
        }
        
        DigestCredential decoded = new DigestCredential(username, null, realm, nonce, opaque, response);
        
        return decoded;
    }
    
    public static String composeNonce() {
        CryptService crypt = new CryptService();
        
        String stamp = new SimpleDateFormat("yyyyMMddHH").format(new Date());
        String nonce = String.valueOf(Long.valueOf(stamp) * 3);
        
        return crypt.hash(nonce);
    }

    public static String composeOpaque() {
        CryptService crypt = new CryptService();
        
        String opaque = "f538a61e972b43deb0813d1e10f7542b";
        
        return crypt.hash(opaque);
    }
    
    public static String composeResponse(String username, String password, String realm, String requestMethod, String requestUri) {
        CryptService crypt = new CryptService();
        
        String mask1 = "{0}:{1}:{2}";
        String mask2 = "{0}:{1}";
        String a1 = crypt.hash(MessageFormat.format(mask1, username, realm, password));
        String a2 = crypt.hash(MessageFormat.format(mask2, requestMethod, requestUri));
        String response = MessageFormat.format(mask1, a1, composeNonce(), a2);
        
        return crypt.hash(response);
    }
}