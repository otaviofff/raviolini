package org.raviolini.domain;

import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Person implements Entity {

    private Integer id;
    private String name;
    private Boolean vegan;
    private Date birthdate;
    
    public Person() {}
    
    public Person(Integer id, String name, Boolean vegan, Date birthdate) {
        this.id = id;
        this.name = name;
        this.vegan = vegan;
        this.birthdate = birthdate;
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public Boolean getVegan() {
        return vegan;
    }
    
    public Date getBirthdate() throws ParseException {
        return birthdate;
    }
    
    @Override
    @JsonIgnore
    public Boolean isValid() {
        return id != null && name != null && vegan != null && birthdate != null;
    }
}