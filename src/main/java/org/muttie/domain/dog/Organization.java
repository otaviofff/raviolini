package org.muttie.domain.dog;

import org.raviolini.domain.entity.Entity;

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