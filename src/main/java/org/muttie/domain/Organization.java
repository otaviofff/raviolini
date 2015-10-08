package org.muttie.domain;

import org.raviolini.domain.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@DatabaseTable(tableName = "organization")
public @Data class Organization implements Entity {

    @DatabaseField(generatedId = true)
    private Integer id;
    
    @DatabaseField
    private String name;
    
    public Organization() {
        //Required by ORM.
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Boolean isValid() {
        return (name != null && !name.isEmpty());
    }
}