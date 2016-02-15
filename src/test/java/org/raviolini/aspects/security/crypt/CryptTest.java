package org.raviolini.aspects.security.crypt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CryptTest {
    
    @Test
    public void testCryptographyMD5() {
        String plain = "plain text phrase";
        String hash = new CryptService().hash(plain);
        
        assertEquals("ed1b0bc8d06c40fd183d5d08cf1ad400", hash);
    }
    
    @Test
    public void testCryptographySHA256() {
        String plain = "plain text phrase";
        String hash = new CryptService().hash(plain, "SHA-256");
        
        assertEquals("c836c0403cfa1a82fc15bd91b3c4c0588257f188a98e1caade6fbe7bcf24fbea", hash);
    }
    
    @Test
    public void testCryptographySHA1() {
        String plain = "plain text phrase";
        String hash = new CryptService().hash(plain, "SHA-1");
        
        assertEquals("6bccf8110916010f10a99ec6093f44b81dbe7d04", hash);
    }
    
    @Test (expected = RuntimeException.class)
    public void testCryptographyException() {
        String plain = "plain text phrase";
        new CryptService().hash(plain, "INVALID");
    }
}