package org.raviolini.aspects.io.logging.drivers;

import java.io.IOException;
import java.util.logging.FileHandler;

public abstract class AbstractFileHandler extends FileHandler {

    public AbstractFileHandler(String pattern) throws IOException, SecurityException {
        super(pattern);
    }
}