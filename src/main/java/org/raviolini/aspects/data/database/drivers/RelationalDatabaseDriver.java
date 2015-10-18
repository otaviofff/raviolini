package org.raviolini.aspects.data.database.drivers;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.domain.Entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

//TODO: Check whether DB table exists.
//More info: https://github.com/j256/ormlite-core/issues/20
//TableUtils.createTableIfNotExists(this.dbConnection, entityClass);

public class RelationalDatabaseDriver<T extends Entity> extends AbstractDatabaseDriver<T> {

    private ConnectionSource databaseConnection;
    private Dao<T, String> database;
    
    public RelationalDatabaseDriver(String engine, String host, Integer port, String name, String user, String pass) {
        super(engine, host, port, name, user, pass);
    }
    
    private String getConnectionString() {
        return MessageFormat.format("jdbc:{0}://{1}:{2}/{3}", getEngine(), getHost(), getPort().toString(), getName());
    }
    
    private ConnectionSource getConnection(Class<T> entityClass) throws SQLException {
        if (databaseConnection == null) {
            databaseConnection =  new JdbcConnectionSource(getConnectionString(), getUser(), getPass());
        }
        
        return databaseConnection;
    }
    
    private Dao<T, String> getDatabase(Class<T> entityClass) throws SQLException {
        if (database == null) {
            database = DaoManager.createDao(getConnection(entityClass), entityClass);
        }
        
        return database;
    }

    @Override
    public List<T> select(Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).queryForAll();
        } catch (SQLException e) {
            throw new DatabaseCommandException();
        }
    }

    @Override
    public T select(Integer entityId, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).queryForId(entityId.toString());
        } catch (SQLException e) {
            throw new DatabaseCommandException();
        }
    }

    @Override
    public Integer insert(T entity, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).create(entity);
        } catch (SQLException e) {
            throw new DatabaseCommandException();
        }
    }

    @Override
    public Integer update(T entity, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).update(entity);
        } catch (SQLException e) {
            throw new DatabaseCommandException();
        }
    }

    @Override
    public Integer delete(Integer entityId, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).deleteById(entityId.toString());
        } catch (SQLException e) {
            throw new DatabaseCommandException();
        }
    }
}