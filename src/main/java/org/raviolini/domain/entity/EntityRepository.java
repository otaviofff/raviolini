package org.raviolini.domain.entity;

import java.sql.SQLException;
import java.util.List;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.service.LoggingService;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class EntityRepository<T extends Entity> {
    
    private Dao<T, String> db;
    private ConnectionSource dbConnection;
    private String dbConnectionUrl;
    private String dbConnectionUsername;
    private String dbConnectionPassword;
    
    public EntityRepository() {
        //TODO: Implement configuration.
        this("jdbc:postgresql://localhost:15432/muttie", "muttie_usr", "muttie_pwd_");
    }
    
    public EntityRepository(String url, String username, String password) {
        this.db = null;
        this.dbConnection = null;
        this.dbConnectionUrl = url;
        this.dbConnectionUsername = username;
        this.dbConnectionPassword = password;
    }

    private ConnectionSource getConnection(Class<T> entityClass) throws SQLException {
        if (this.dbConnection == null) {
            this.dbConnection =  new JdbcConnectionSource(
                    this.dbConnectionUrl, 
                    this.dbConnectionUsername, 
                    this.dbConnectionPassword
                    );
            
            //TODO: Check whether DB exists.
            //More info: https://github.com/j256/ormlite-core/issues/20
            //TableUtils.createTableIfNotExists(this.dbConnection, entityClass);
        }
        
        return this.dbConnection;
    }
    
    private Dao<T, String> getDatabase(Class<T> entityClass) throws SQLException {
        if (this.db == null) {
            this.db = DaoManager.createDao(getConnection(entityClass), entityClass);
        }
        
        return this.db;
    }
    
    public List<T> select(Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).queryForAll();
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public T select(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).queryForId(entityId.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer insert(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).create(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer update(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).update(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer delete(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).deleteById(entityId.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    private void logException(Exception e) {
        LoggingService logger = new LoggingService();
        logger.logException(e);
    }
}