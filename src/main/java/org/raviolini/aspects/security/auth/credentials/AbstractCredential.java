package org.raviolini.aspects.security.auth.credentials;

public abstract class AbstractCredential {
    private String username;
    private String password;
    
    public AbstractCredential(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    @Override
    public boolean equals(Object obj) {
        try {
            AbstractCredential tested = (AbstractCredential) obj;
            
            return username.equals(tested.getUsername()) &&
                   password.equals(tested.getPassword());
        } catch (ClassCastException e) {
            return false;
        }
    }
}