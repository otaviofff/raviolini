package org.raviolini.aspects.data.database.drivers;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

import org.raviolini.aspects.data.database.exceptions.DatabaseCommandException;
import org.raviolini.aspects.data.database.helpers.RelationalDatabaseQuery;
import org.raviolini.domain.Entity;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class RelationalDatabaseDriver<T extends Entity> extends AbstractDatabaseDriver<T> {

    private ConnectionSource databaseConnection;
    private Dao<T, String> database;
    
    public RelationalDatabaseDriver(String engine, String host, Integer port, String name, String user, String pass, Boolean boot) {
        super(engine, host, port, name, user, pass, boot);
    }
    
    private String getConnectionString() {
        return MessageFormat.format("jdbc:{0}://{1}:{2}/{3}", getEngine(), getHost(), getPort().toString(), getName());
    }
    
    /**
     * Instantiates a properly configured JdbcConnectionSource object.
     * 
     * Note that this method also bootstraps the database table associated with
     *  the entity class in question. However, bootstrapping will only take
     *  place if the API configuration says so. This conditional is meant to
     *  avoid an issue with ORM Lite, which will fail if the data-table has 
     *  already been created earlier.
     *  
     * More on this ORM issue can be found at: 
     *  https://github.com/j256/ormlite-core/issues/20
     * 
     * @param Class<T> entityClass
     * @return ConnectionSource
     * @throws SQLException
     */
    private ConnectionSource getConnection(Class<T> entityClass) throws SQLException {
        if (databaseConnection == null) {
            databaseConnection =  new JdbcConnectionSource(getConnectionString(), getUser(), getPass());
            
            if (getBoot()) {
                TableUtils.createTableIfNotExists(databaseConnection, entityClass);
            }
        }
        
        return databaseConnection;
    }
    
    private Dao<T, String> getDatabase(Class<T> entityClass) throws SQLException {
        if (database == null) {
            database = DaoManager.createDao(getConnection(entityClass), entityClass);
        }
        
        return database;
    }
    
    private RelationalDatabaseQuery<T> getQuery(HashMap<String, String> params, Boolean countOnly, Class<T> entityClass) throws SQLException {
        QueryBuilder<T, String> builder;
        
        builder = getDatabase(entityClass).queryBuilder();
        builder.setCountOf(countOnly);
        
        return new RelationalDatabaseQuery<T>(builder, params);
    }

    @Override
    public List<T> select(Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).queryForAll();
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("SELECT", e);
        }
    }
    
    @Override
    public List<T> select(HashMap<String, String> params, Class<T> entityClass) throws DatabaseCommandException {
        try {
            RelationalDatabaseQuery<T> query = getQuery(params, false, entityClass);
            
            return getDatabase(entityClass).query(query.prepare());
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("SELECT", e);
        }
    }

    @Override
    public T select(Integer entityId, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).queryForId(entityId.toString());
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("SELECT", e);
        }
    }

    @Override
    public Integer insert(T entity, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).create(entity);
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("INSERT", e);
        }
    }

    @Override
    public Integer update(T entity, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).update(entity);
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("UPDATE", e);
        }
    }

    @Override
    public Integer delete(Integer entityId, Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).deleteById(entityId.toString());
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("DELETE", e);
        }
    }

    @Override
    public Long count(Class<T> entityClass) throws DatabaseCommandException {
        try {
            return getDatabase(entityClass).countOf();
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("COUNT", e);
        }
    }
    
    @Override
    public Long count(HashMap<String, String> params, Class<T> entityClass) throws DatabaseCommandException {
        try {
            RelationalDatabaseQuery<T> query = getQuery(params, true, entityClass);
            
            return getDatabase(entityClass).countOf(query.prepare());
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseCommandException("COUNT", e);
        }
    }
}