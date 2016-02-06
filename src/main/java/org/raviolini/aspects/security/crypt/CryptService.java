package org.raviolini.aspects.security.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptService {

    public String hash(String input) {
        return hash(input, "MD5");
    }
    
    public String hash(String input, String algorithm) {
        String charset = "UTF-8";
        
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(input.getBytes(charset));
            
            return toString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm " + algorithm + " not supported in package Security.");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Charset " + charset + " not supported in package Security.");
        }
    }
    
    private String toString(byte[] digest) {
        StringBuffer buffer = new StringBuffer();
        
        for (byte b : digest) {
            buffer.append(String.format("%02x", b & 0xff));
        }
        
        return buffer.toString();
    }
}