package org.raviolini.aspects.security.auth;

public class Credential {

    private final String user;
    private final String pass;
    
    public Credential(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
    
    public String getUsername() {
        return user;
    }
    
    public String getPassword() {
        return pass;
    }
}