package org.raviolini.service.generic;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.raviolini.api.exception.InternalServerException;
import org.raviolini.domain.Entity;
import org.raviolini.service.io.ConfigurationService;
import org.raviolini.service.io.LoggingService;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseService<T extends Entity> {
    
    private Dao<T, String> db;
    private ConnectionSource dbConnection;

    //TODO: Check whether DB exists.
    //More info: https://github.com/j256/ormlite-core/issues/20
    //TableUtils.createTableIfNotExists(this.dbConnection, entityClass);
    
    private Map<String, String> getProperties() throws IOException {
        //TODO: Move to super class.
        String namespace = "raviolini.database";
        String[] keys = new String[] {"driver", "host", "port", "name", "user", "pass"};
        
        ConfigurationService config = new ConfigurationService();
        
        return config.read(namespace, keys);
    }
    
    private ConnectionSource getConnection(Class<T> entityClass) throws SQLException, IOException {
        if (dbConnection == null) {
            Map<String, String> properties = getProperties();

            String url = MessageFormat.format("jdbc:{0}://{1}:{2}/{3}", 
                    properties.get("driver"),
                    properties.get("host"),
                    properties.get("port"),
                    properties.get("name")
            );
            
            String user = properties.get("user");
            String pass = properties.get("pass");
            
            dbConnection =  new JdbcConnectionSource(url, user, pass);
        }
        
        return dbConnection;
    }
    
    private Dao<T, String> getDatabase(Class<T> entityClass) throws SQLException, IOException {
        if (db == null) {
            db = DaoManager.createDao(getConnection(entityClass), entityClass);
        }
        
        return db;
    }
    
    public List<T> select(Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).queryForAll();
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException("Database failed to SELECT data.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Database config failed.");
        }
    }
    
    public T select(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).queryForId(entityId.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException("Database failed to SELECT data.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Database config failed.");
        }
    }
    
    public Integer insert(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).create(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException("Database failed to INSERT data.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Database config failed.");
        }
    }
    
    public Integer update(T entity, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).update(entity);
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException("Database failed to UPDATE data.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Database config failed.");
        }
    }
    
    public Integer delete(Integer entityId, Class<T> entityClass) throws InternalServerException {
        try {
            return getDatabase(entityClass).deleteById(entityId.toString());
        } catch (SQLException e) {
            logException(e);
            throw new InternalServerException("Database failed to DELETE data.");
        } catch (IOException e) {
            logException(e);
            throw new InternalServerException("Database config failed.");
        }
    }
    
    private void logException(Exception e) {
        //TODO: Move to super class.
        //Avoid creating a new service at every method call.
        LoggingService logger = new LoggingService();
        logger.logException(e, true);
    }
}