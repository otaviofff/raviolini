package org.raviolini.aspects.io.serialization.drivers;

import org.raviolini.domain.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerializationDriver<T extends Entity> extends AbstractSerializationDriver<T> {

    @Override
    protected ObjectMapper getObjectMapper() {
        return new XmlMapper();
    }
}