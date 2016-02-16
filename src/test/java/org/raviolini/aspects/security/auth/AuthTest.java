package org.raviolini.aspects.security.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class AuthTest {
    
    @Test
    public void testBasicAuthentication() throws UnloadableConfigException, InvalidPropertyException {
        AuthService auth = new AuthService();
        
        assertEquals("Basic realm=\"Impenetrable\"", auth.challenge());
        assertTrue(auth.authenticate("GET", "/person", "Basic YXBpX3VzZXI6YXBpX3Bhc3M="));
        assertFalse(auth.authenticate("GET", "/person", "Basic YXBpX3VzZXJfaW52YWxpZDphcGlfcGFzc19pbnZhbGlk"));
    }
    
    /**
     * Tests HTTP digest access authentication.
     * 
     * Authentication nonce is valid within the hour only. Thus, in order
     *  to keep this unit test from failing, if the current hour is about
     *  to end, we then put the thread to sleep for 5 seconds, which will
     *  let another hour begin.
     * 
     * @throws UnloadableConfigException
     * @throws InvalidPropertyException
     * @throws InterruptedException
     */
    @Test
    public void testDigestAuthentication() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        Calendar now = Calendar.getInstance();
        Integer min = now.get(Calendar.MINUTE);
        Integer sec = now.get(Calendar.SECOND);
        
        if (min > 58 && sec > 55) {
            Thread.sleep(5000);
        }
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user", "api_pass", "GET", "/person");
        
        String encodedCredential = proxy.encodeCredential(auth.challenge());
        
        assertTrue(auth.authenticate("GET", "/person", encodedCredential));
    }
    
    @Test
    public void testAuthorization() throws UnloadableConfigException, InvalidPropertyException {
        AuthService auth = new AuthService();      
        
        assertTrue(auth.authorize("GET"));
        assertTrue(auth.authorize("POST"));
        assertFalse(auth.authorize("PUT"));
        assertFalse(auth.authorize("DELETE"));
    }
}