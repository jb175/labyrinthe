package com.isep.jbmo60927.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingFormatArgumentException;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * formatter used for the log
 */
public class MyFormatter extends Formatter {

    //components we could display on log
    private enum LogComponents {
        DATE,
        CLASSNAME,
        METHODNAME,
        LEVEL,
        MESSAGE
    }

    private static final LogComponents[] COMPONENTS = new LogComponents[] {
        LogComponents.DATE,
        LogComponents.CLASSNAME,
        LogComponents.METHODNAME,
        LogComponents.LEVEL,
        LogComponents.MESSAGE
    };

    private static final String LOG_FORMAT = "[%s][%s %s] %s: %s"; //how to display a log (one %s per LogComponent in COMPONENTS array)
    private static final Boolean ADD_ERROR_ON_LOG = true; //should we display the error
    private static final String ERROR_LOG_FORMAT = ": %s"; //how to display the error (this will be added at the end of the LOG_FORMAT)
    private static final Boolean ADD_ERROR_STACK_TRACES_ON_LOG = true; //should we display stack trace from errors
    private static final String ERROR_STACK_TRACE_FORMAT = "        %s"; //alaways one new line for every stack trace
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"; //see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    
    public MyFormatter() {
        super();
    }

    /**
     * this method is called for every log records
     * [Date][ClassName <MethodName>] Level: Message
     */
    public String format(final LogRecord rec) {
        //log builder
        StringBuilder log = new StringBuilder();

        //log error
        final Throwable errorCause = rec.getThrown();

        //main log
        log.append(formatLog(COMPONENTS, rec));
        
        //error
        if (Boolean.TRUE.equals(ADD_ERROR_ON_LOG) && errorCause != null)
            log.append(String.format(ERROR_LOG_FORMAT, errorCause.getMessage()));
        
        log.append("\n");
        //stack trace
        if (Boolean.TRUE.equals(ADD_ERROR_STACK_TRACES_ON_LOG) && errorCause != null) {
            for (StackTraceElement stackTraceElement : errorCause.getStackTrace()) {
                log.append(String.format(ERROR_STACK_TRACE_FORMAT+"%n", stackTraceElement.toString()));
            }
        }

        //complete log
        return log.toString();
    }

    /**
     * format the main part of the log (we can't use String.format because we have an unknow number or components to display)
     * @param components the components to display
     * @param rec the record we wants to extract data from
     * @return String with the main part of the log
     */
    private static String formatLog(LogComponents[] components, LogRecord rec) {

        //we try to verify if there is the exact number of components given for this log
        Boolean formatBeginWithComponent = LOG_FORMAT.substring(0, 2).equals("%s");
        Boolean formatEndWithComponent = LOG_FORMAT.substring(LOG_FORMAT.length()-2, LOG_FORMAT.length()).equals("%s");
        
        int formatParts = LOG_FORMAT.split("%s").length;
        if (Boolean.TRUE.equals(formatBeginWithComponent))
            formatParts++;
        if (Boolean.TRUE.equals(formatEndWithComponent))
            formatParts++;
        
        //if there is not enought or too much arguments
        if (formatParts != components.length+1)
            throw new MissingFormatArgumentException("The number of format %s from LOG_FORMAT is different from the number of components given in COMPONENTS");
        

        StringBuilder log = new StringBuilder();

        //if the format begin with a component
        if (Boolean.TRUE.equals(formatBeginWithComponent)) {
            //we add the first component
            log.append(LOG_FORMAT.split("%s")[0]);
            //and every other componenets
            for (int i = 1; i < components.length; i++) {
                log.append(LOG_FORMAT.split("%s")[i-1]);
                log.append(getRecordData(rec, components[i]));
            }
        } else {
            //otherwise we format normally
            for (int i = 0; i < components.length; i++) {
                log.append(LOG_FORMAT.split("%s")[i]);
                log.append(getRecordData(rec, components[i]));
            }
        }
        //if the format end with a component we add it at the end
        if (Boolean.FALSE.equals(formatEndWithComponent))
            log.append(LOG_FORMAT.split("%s")[LOG_FORMAT.split("%s").length-1]);

        return log.toString();
    }

    /**
     * get the data from the components
     * @param rec the record we wants to extract data from
     * @param component the component we wants to display
     * @return String to display
     */
    private static String getRecordData(LogRecord rec, LogComponents component) {
        switch (component) {
            case DATE:
                return dateFormatter(rec.getMillis());
            case CLASSNAME:
                return rec.getSourceClassName();
            case METHODNAME:
                return rec.getSourceMethodName();
            case LEVEL:
                return rec.getLevel().toString();
            case MESSAGE:
                return rec.getMessage();
            default:
                return "";
        }
    }

    /**
     * date formatter for the log
     * @param millisecs current time used for the file name
     * @return a string with the date correctly displayed
     */
    private static String dateFormatter(final long millisecs) {
        final Date resultDate = new Date(millisecs);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(resultDate);
    }
}