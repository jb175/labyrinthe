package com.isep.jbmo60927.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * setup the logger for the app
 */
public class MyLogger {

    private static final String LOG_FOLDER = "log";
    private static final String LOG_FILE_NAME = "labyrinthe";
    private static final String LOG_FILE_NAME_SEPARATOR = "_";
    private static final Boolean DATE_ON_LOG_FILE = true;
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss"; //see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    private static final String LOG_FILE_EXTENSION = "log"; //extension without .

    /**
     * Default constructor
     * @throws IllegalStateException the constructor is not used
     */
    private MyLogger() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * setup the logger for the app
     * @throws IOException if something bad occure
     */
    public static void setup() throws IOException {
        //root logger
        final Logger rootLogger = Logger.getLogger("");
        
        //create file log handler
        FileHandler logfile;
        StringBuilder logPath = new StringBuilder();
        final long time = System.currentTimeMillis();

        //generate file name
        logPath.append(LOG_FOLDER+"/"+LOG_FILE_NAME);
        if (Boolean.TRUE.equals(DATE_ON_LOG_FILE))
            logPath.append(LOG_FILE_NAME_SEPARATOR+dateFormatter(time));
        if (!"".equals(LOG_FILE_EXTENSION))
            logPath.append("."+LOG_FILE_EXTENSION);

        //create the file (and folder if not previously created)
        try {
            logfile = new FileHandler(logPath.toString()); //we try to create the log file
        } catch (final NoSuchFileException e) { //if the log folder doesn't exist
            new File(LOG_FOLDER).mkdirs(); //we create the folder
            logfile = new FileHandler(logPath.toString()); //and create the log file
        }

        //we add the file log handler to the root logger
        rootLogger.addHandler(logfile);

        //apply the custom formatter to all rootlogger's handeler
        for (final Handler handler : rootLogger.getHandlers()) {
            handler.setFormatter(new MyFormatter());
        }
    }

    /**
     * date formatter for the file name of the log
     * @param millisecs current time used for the file name
     * @return a string with the date correctly displayed
     */
    private static String dateFormatter(final long millisecs) {
        final Date resultDate = new Date(millisecs);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(resultDate);
    }
}