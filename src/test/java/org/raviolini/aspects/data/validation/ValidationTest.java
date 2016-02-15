package org.raviolini.aspects.data.validation;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.raviolini.aspects.data.validation.exceptions.InvalidEntityException;
import org.raviolini.domain.Person;

public class ValidationTest {
    
    @Test
    public void testValidEntity() throws ParseException, InvalidEntityException {
        Person person = new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03"));
        Boolean valid = new ValidationService().validate(person);
        
        assertTrue(valid);
    }
    
    @Test (expected = InvalidEntityException.class)
    public void testInvalidEntity() throws InvalidEntityException {
        Person person = new Person();
        new ValidationService().validate(person);
    }
}