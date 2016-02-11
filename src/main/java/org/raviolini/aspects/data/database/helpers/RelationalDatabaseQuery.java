package org.raviolini.aspects.data.database.helpers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.raviolini.aspects.data.database.exceptions.InvalidQueryException;
import org.raviolini.domain.Entity;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class RelationalDatabaseQuery<T extends Entity> {
    
    private QueryBuilder<T, String> builder;
    private HashMap<String, String> params;
    private Boolean defaultSorting;
    private Long defaultLimit;
    private Long defaultOffset;
    
    public RelationalDatabaseQuery(QueryBuilder<T, String> builder, HashMap<String, String> params) {
        this.builder = builder;
        this.params = params;
        this.defaultSorting = true;
        this.defaultLimit = (long) 100;
        this.defaultOffset = (long) 0;
    }
    
    public PreparedQuery<T> prepare() throws SQLException {
        try {
            setPagination();
            setOrder();
            setFilter(); // Must be the last method call in this chain.
        } catch (IllegalArgumentException e) {
            throw new InvalidQueryException(e.getMessage(), e);
        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidQueryException(e);
        }
        
        return builder.prepare();
    }
    
    private void setPagination() throws SQLException {
        Long value;
        
        if (params.containsKey("limit")) {
            value = Long.valueOf(params.get("limit"));
            builder.limit(value);
            params.remove("limit");
        } else {
            builder.limit(defaultLimit);
        }
        
        if (params.containsKey("offset")) {
            value = Long.valueOf(params.get("offset"));
            builder.offset(value);
            params.remove("offset");
        } else {
            builder.offset(defaultOffset);
        }
    }
    
    private void setOrder() {
        if (!params.containsKey("orderby")) {
            return;
        }
           
        String field = params.get("orderby").trim();
        StringTokenizer tokenizer = new StringTokenizer(field, ",");
        
        while (tokenizer.hasMoreTokens()) {
            appendOrder(tokenizer.nextToken().trim());
        }
        
        params.remove("orderby");
    }
    
    private void appendOrder(String field) {
        switch (field.charAt(0)) {
            case '-':
                field = field.substring(1);
                builder.orderBy(field, false);
                break;
            case '+':
                field = field.substring(1);
                builder.orderBy(field, true);
                break;
            default:
                builder.orderBy(field, defaultSorting);
                break;
        }
    }
    
    private void setFilter() throws SQLException {
        if (params.isEmpty()) {
            return;
        }
        
        Where<T, String> where = builder.where();
        
        for (String field : params.keySet()) {
            appendFilter(field, where);
        }
        
        where.and(params.keySet().size());
    }
    
    private void appendFilter(String field, Where<T, String> where) throws SQLException {
        String value = params.get(field);
        
        if (value.charAt(0) == '~') {
            //Keyword ILIKE should be used for case-insensitive matching.
            //Works for both substring (with %) and string searching.
            //This isn't SQL standard, but is a PostgreSQL extension.
            where.rawComparison(field, "ILIKE", value.substring(1));
        } else if (value.charAt(0) == '%' || value.charAt(value.length() - 1) == '%') {
            //Keyword LIKE should be used for substring searching only.
            //Case sensitive in PostgreSQL, but insensitive in MySQL.
            //Percent symbol (%) must come URL-encoded (%25).
            where.like(field, value);
        } else {
            //Operator '=' should be used for exact matching only.
            //Case sensitive in PostgreSQL, but insensitive in MySQL.
            where.eq(field, value);
        }
    }
}