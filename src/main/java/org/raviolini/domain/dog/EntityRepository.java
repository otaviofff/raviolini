package org.raviolini.domain.dog;

import java.sql.SQLException;
import java.util.List;

import org.raviolini.api.exception.InternalServerException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class EntityRepository {
    
    private Dao<Dog, String> db;
    private ConnectionSource dbConnection;
    private String dbConnectionUrl;
    private String dbConnectionUsername;
    private String dbConnectionPassword;
    
    public EntityRepository() {
        //TODO: Implement configuration.
        this("jdbc:postgresql://localhost:15432/muttie", "muttie_usr", "muttie_pwd");
    }
    
    public EntityRepository(String url, String username, String password) {
        this.db = null;
        this.dbConnection = null;
        this.dbConnectionUrl = url;
        this.dbConnectionUsername = username;
        this.dbConnectionPassword = password;
    }

    private ConnectionSource getConnection() throws SQLException {
        if (this.dbConnection == null) {
            this.dbConnection =  new JdbcConnectionSource(
                    this.dbConnectionUrl, 
                    this.dbConnectionUsername, 
                    this.dbConnectionPassword
                    );
            
            //TODO: Check whether DB exists.
            //More info: https://github.com/j256/ormlite-core/issues/20
            //TableUtils.createTableIfNotExists(this.dbConnection, Dog.class);
        }
        
        return this.dbConnection;
    }
    
    private Dao<Dog, String> getDatabase() throws SQLException {
        if (this.db == null) {
            this.db = DaoManager.createDao(getConnection(), Dog.class);
        }
        
        return this.db;
    }
    
    public List<Dog> select() throws InternalServerException {
        try {
            return getDatabase().queryForAll();
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Dog select(Integer id) throws InternalServerException {
        try {
            return getDatabase().queryForId(id.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer insert(Dog entity) throws InternalServerException {
        try {
            return getDatabase().create(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer update(Dog entity) throws InternalServerException {
        try {
            return getDatabase().update(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    public Integer delete(Integer id) throws InternalServerException {
        try {
            return getDatabase().deleteById(id.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException(e.getMessage());
        }
    }
    
    private void logException(Exception e) {
        //TODO: Implement logging.
        e.printStackTrace();
    }
}