package org.raviolini.aspects.security.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.raviolini.aspects.io.configuration.ConfigRegistry;
import org.raviolini.aspects.io.configuration.drivers.FileConfigDriver;
import org.raviolini.aspects.io.configuration.exceptions.InvalidPropertyException;
import org.raviolini.aspects.io.configuration.exceptions.UnloadableConfigException;

public class AuthTest {
    
    @Test
    public void testBasicAuthentication() throws UnloadableConfigException, InvalidPropertyException {
        ConfigRegistry.getInstance().set(new FileConfigDriver("auth.basic.properties"));
        AuthService auth = new AuthService();
        
        assertEquals("Basic realm=\"secured\"", auth.challenge());
        assertTrue(auth.authenticate("GET", "/person", "Basic YXBpX3VzZXI6YXBpX3Bhc3M="));
        assertFalse(auth.authenticate("GET", "/person", "Basic YXBpX3VzZXJfaW52YWxpZDphcGlfcGFzc19pbnZhbGlk"));
        
        ConfigRegistry.getInstance().reset();
    }
    
    @Test
    public void testDigestAuthentication() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        sleep();
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user", "api_pass", "GET", "/person");
        
        String encodedCredential = proxy.encodeCredential(auth.challenge());
        
        assertTrue(auth.authenticate("GET", "/person", encodedCredential));
    }
    
    @Test
    public void testDigestAuthenticationWithInvalidUsername() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        sleep();
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user_invalid", "api_pass", "GET", "/person");
        
        String encodedCredential = proxy.encodeCredential(auth.challenge());

        assertFalse(auth.authenticate("GET", "/person", encodedCredential));
    }
    
    @Test
    public void testDigestAuthenticationWithInvalidPassword() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        sleep();
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user", "api_pass_invalid", "GET", "/person");
        
        String encodedCredential = proxy.encodeCredential(auth.challenge());

        assertFalse(auth.authenticate("GET", "/person", encodedCredential));
    }
    
    @Test
    public void testDigestAuthenticationWithInvalidScope() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        sleep();
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user", "api_pass", "GET", "/person");
        
        String encodedCredential = proxy.encodeCredential(auth.challenge());

        assertFalse(auth.authenticate("GET", "/invalid", encodedCredential));
        assertFalse(auth.authenticate("PUT", "/person", encodedCredential));
        assertFalse(auth.authenticate("POST", "/person", encodedCredential));
        assertFalse(auth.authenticate("DELETE", "/person", encodedCredential));
    }  
    
    @Test
    public void testDigestAuthenticationWithInvalidResponse() throws UnloadableConfigException, InvalidPropertyException, InterruptedException {
        sleep();
        
        AuthService auth = new AuthService();
        AuthClient proxy = new AuthClient("api_user", "api_pass", "GET", "/person");
        
        String encodedCredential;
        
        encodedCredential = proxy.encodeCredential(auth.challenge());
        encodedCredential = encodedCredential.substring(0, encodedCredential.indexOf("response") - 1);
        
        assertFalse(auth.authenticate("GET", "/person", encodedCredential));
    }
    
    @Test
    public void testAuthorization() throws UnloadableConfigException, InvalidPropertyException {
        AuthService auth = new AuthService();      
        
        assertTrue(auth.authorize("GET"));
        assertTrue(auth.authorize("POST"));
        assertFalse(auth.authorize("PUT"));
        assertFalse(auth.authorize("DELETE"));
    }
    
    /**
     * Pauses the execution of for 5 seconds.
     * 
     * Authentication nonce is valid within the hour only. Thus, in order
     *  to keep these unit tests from failing, if the current hour is about
     *  to end, this method then puts this thread to sleep for 5 seconds, 
     *  which will allow for another hour to begin.
     *  
     * @throws InterruptedException
     */
    private void sleep() throws InterruptedException {
        Calendar now = Calendar.getInstance();
        Integer min = now.get(Calendar.MINUTE);
        Integer sec = now.get(Calendar.SECOND);
        
        if (min > 58 && sec > 55) {
            Thread.sleep(5000);
        }
    }
}