package org.raviolini.aspects.io.logging.drivers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;;

/***
 * Log handler with dated file name.
 * 
 * @author otaviofff
 */
public class DatedFileHandler extends AbstractFileHandler {

    /***
     * Constructs a log handler, with dated file name, with the purpose of
     *  facilitating further troubleshooting.
     *  
     * Note though that limit will always be set to 0, and count to 1,
     *  regardless of other values defined in the app's configuration
     *  file. It isn't possible to override these attributes, because only the
     *  empty constructor can be called for handlers defined in configuration,
     *  which sets both values in a hard-coded manner.
     *  
     * As a result, there will be only one log file created per month,
     *  and no file size limit in bytes, which is actually the most common
     *  use case anyway. For other use cases, another implementation of
     *  AbstractFileHandler will be required.
     * 
     * @throws SecurityException
     * @throws IOException
     */
    public DatedFileHandler() throws SecurityException, IOException {
        super(getPattern());
    }
    
    /***
     * Gets the log file name, dated to the current month and year.
     * 
     * Example: log/raviolini_201601.log
     * 
     * @return String
     */
    private static String getPattern() {
        return ("log/raviolini_")
                .concat(new SimpleDateFormat("yyyyMM").format(new Date()))
                .concat(".log");
    }
}