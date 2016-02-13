package org.raviolini.aspects.data.database.drivers;

import java.util.HashMap;
import java.util.List;

import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.domain.Entity;

public abstract class AbstractDatabaseDriver<T extends Entity> {

    private String  engine;
    private String  host;
    private Integer port;
    private String  name;
    private String  user;
    private String  pass;
    private Boolean boot;
    
    public AbstractDatabaseDriver(String engine, String host, Integer port, String name, String user, String pass, Boolean boot) {
        this.engine = engine;
        this.host = host;
        this.port = port;
        this.name = name;
        this.user = user;
        this.pass = pass;
        this.boot = boot;
    }
    
    public String getEngine() {
        return engine;
    }
    
    public String getHost() {
        return host;
    }
    
    public Integer getPort() {
        return port;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUser() {
        return user;
    }
    
    public String getPass() {
        return pass;
    }
    
    public Boolean getBoot() {
        return boot;
    }
    
    public abstract List<T> select(Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract List<T> select(HashMap<String, String> params, Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract T select(Integer entityId, Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract Integer insert(T entity, Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract Integer update(T entity, Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract Integer delete(Integer entityId, Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract Long count(Class<T> entityClass) throws DatabaseCommandException;
    
    public abstract Long count(HashMap<String, String> params, Class<T> entityClass) throws DatabaseCommandException;
}
