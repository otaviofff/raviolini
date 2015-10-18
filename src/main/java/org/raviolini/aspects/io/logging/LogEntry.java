package org.raviolini.aspects.io.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogEntry {
    
    public static String composeStackTraceEntry(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        e.printStackTrace(pw);
        
        return sw.toString();
    }
    
    public static String composeExceptionEntry(Exception e) {
        return e.getMessage().concat("\n");
    }
}