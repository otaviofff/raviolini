package org.raviolini.aspects.io.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.raviolini.aspects.io.serialization.exceptions.SerializationException;
import org.raviolini.aspects.io.serialization.exceptions.UnserializationException;
import org.raviolini.domain.Person;

public class SerializationTest {

    @Test
    public void testJsonListSerialization() throws SerializationException, ParseException {
        List<Person> list = new ArrayList<>();
        list.add(new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03")));
        list.add(new Person(2, "Eduardo", true, new SimpleDateFormat("yyyy/MM/dd").parse("2016/03/11")));
        
        String serialized = new SerializationService<Person>("application/json").serialize(list);
        
        assertEquals(
            "[{\"id\":1,\"name\":\"Otavio\",\"vegan\":false,\"birthdate\":452401200000}," +
            "{\"id\":2,\"name\":\"Eduardo\",\"vegan\":true,\"birthdate\":1457665200000}]",
            serialized
        );
    }
    
    @Test
    public void testJsonSerialization() throws SerializationException, ParseException {
        Person person = new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03"));
        String serialized = new SerializationService<Person>("application/json").serialize(person);
        
        assertEquals("{\"id\":1,\"name\":\"Otavio\",\"vegan\":false,\"birthdate\":452401200000}", serialized);
    }
    
    @Test
    public void testJsonUnserialization() throws UnserializationException, ParseException {
        String serialized = "{\"name\":\"Otavio\",\"id\":1,\"vegan\":false,\"birthdate\":452401200000}";
        Person unserialized = new SerializationService<Person>("application/json").unserialize(serialized, Person.class);
        
        assertEquals(1L, (long) unserialized.getId());
        assertEquals("Otavio", unserialized.getName());
        assertFalse(unserialized.getVegan());
        assertEquals(452401200000L, unserialized.getBirthdate().getTime());
    }
    
    @Test (expected = UnserializationException.class)
    public void testJsonUnserializationException() throws UnserializationException {
        String serialized = "\"name\":\"Otavio\",\"id\":1,\"vegan\":false,\"birthdate\":452401200000}";
        new SerializationService<Person>("application/json").unserialize(serialized, Person.class);
    }
    
    @Test
    public void testXmlListSerialization() throws SerializationException, ParseException {
        List<Person> list = new ArrayList<>();
        list.add(new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03")));
        list.add(new Person(2, "Eduardo", true, new SimpleDateFormat("yyyy/MM/dd").parse("2016/03/11")));
        
        String serialized = new SerializationService<Person>("application/xml").serialize(list);
        
        assertEquals(
            "<ArrayList><item><id>1</id><name>Otavio</name><vegan>false</vegan><birthdate>452401200000</birthdate></item>" +
            "<item><id>2</id><name>Eduardo</name><vegan>true</vegan><birthdate>1457665200000</birthdate></item></ArrayList>",
            serialized
        );
    }
    
    @Test
    public void testXmlSerialization() throws SerializationException, ParseException {
        Person person = new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03"));
        String serialized = new SerializationService<Person>("application/xml").serialize(person);
        
        assertEquals("<Person><id>1</id><name>Otavio</name><vegan>false</vegan><birthdate>452401200000</birthdate></Person>", serialized);
    }
    
    @Test
    public void testXmlUnserialization() throws UnserializationException, ParseException {
        String serialized = "<Person><name>Otavio</name><id>1</id><vegan>false</vegan><birthdate>452401200000</birthdate></Person>";
        Person unserialized = new SerializationService<Person>("application/xml").unserialize(serialized, Person.class);
        
        assertEquals(1L, (long) unserialized.getId());
        assertEquals("Otavio", unserialized.getName());
        assertFalse(unserialized.getVegan());
        assertEquals(452401200000L, unserialized.getBirthdate().getTime());
    }
    
    @Test (expected = UnserializationException.class)
    public void testXmlUnserializationException() throws UnserializationException {
        String serialized = "<name>Otavio</name><id>1</id><vegan>false</vegan><birthdate>452401200000</birthdate></Person>";
        new SerializationService<Person>("application/xml").unserialize(serialized, Person.class);
    }
    
    @Test (expected = RuntimeException.class)
    public void testUnsupportedMediaType() throws SerializationException, ParseException {
        Person person = new Person(1, "Otavio", false, new SimpleDateFormat("yyyy/MM/dd").parse("1984/05/03"));
        new SerializationService<Person>("text/html").serialize(person);
    }
}