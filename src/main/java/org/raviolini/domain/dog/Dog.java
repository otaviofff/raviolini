package org.raviolini.domain.dog;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@DatabaseTable(tableName = "dog")
public @Data class Dog {
    
    @DatabaseField(generatedId = true)
    private Integer id;
    
    @DatabaseField
    private String name;
    
    @DatabaseField
    private Boolean neutered;
    
    public Dog() {
    }
    
    public Boolean isValid() {
        return (neutered != null && name != null && !name.isEmpty());
    }
}