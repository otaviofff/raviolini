package org.raviolini.aspects.security.auth.drivers;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.raviolini.aspects.security.crypt.CryptService;

public class DigestAuthDriver extends AbstractAuthDriver {

    private CryptService crypt;
    
    public DigestAuthDriver(String user, String pass, List<String> methods) {
        super(user, pass, methods);
        crypt = new CryptService();
    }
    
    @Override
    public String getName() {
        return "Digest";
    }
    
    private String getNonce() {
        DateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        String stamp = formatter.format(new Date());
        String nonce = String.valueOf(Long.valueOf(stamp) * 3);
        
        return crypt.hash(nonce);
    }

    private String getOpaque() {
        String opaque = "f538a61e972b43deb0813d1e10f7542b";
        
        return crypt.hash(opaque);
    }
    
    private String getResponse(String requestMethod, String requestUri) {
        String mask1, mask2, a1, a2, rp;
        
        mask1 = "{0}:{1}:{2}";
        mask2 = "{0}:{1}";
        a1 = crypt.hash(MessageFormat.format(mask1, getUsername(), getRealm(), getPassword()));
        a2 = crypt.hash(MessageFormat.format(mask2, requestMethod, requestUri));
        rp = crypt.hash(MessageFormat.format(mask1, a1, getNonce(), a2));
        
        return rp;
    }
    
    @Override
    public String getChallenge() {
        String mask = "{0} realm=\"{1}\", nonce=\"{2}\", opaque=\"{3}\"";
        
        return MessageFormat.format(mask, getName(), getRealm(), getNonce(), getOpaque());
    }
    
    @Override
    protected Boolean matchCredential(String requestMethod, String requestUri, String encodedCredential) {
        StringTokenizer tokenizer = new StringTokenizer(encodedCredential, ",");

        Pattern pattern = Pattern.compile("(.*)=\"([^\"]*)\"");
        Matcher match;
        
        String token, field, value;
        String receivedUsername = "";
        String receivedRealm = "";
        String receivedNonce = "";
        String receivedOpaque = "";
        String receivedResponse = "";
        
        while (tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken().trim();
            match = pattern.matcher(token);
            
            if (match.matches() == false || match.groupCount() != 2) {
                return false;
            }
            
            field = match.group(1).trim().toLowerCase();
            value = match.group(2).trim();
            
            switch (field) {
                case "username":
                    receivedUsername = value;
                    break;
                case "realm":
                    receivedRealm = value;
                    break;
                case "nonce":
                    receivedNonce = value;
                    break;
                case "opaque":
                    receivedOpaque = value;
                    break;
                case "response":
                    receivedResponse = value;
                    break;
            }
        }
        
        return receivedUsername.equals(getUsername())
            && receivedRealm.equals(getRealm())
            && receivedNonce.equals(getNonce())
            && receivedOpaque.equals(getOpaque())
            && receivedResponse.equals(getResponse(requestMethod, requestUri));
    }
}