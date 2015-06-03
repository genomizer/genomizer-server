package model;

/**
 * Class which is used to log the error messages for the tests.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ErrorLogger {

    /**
     * Logs the message.
     * @param message Message to log.
     */
    public static void log(String message){
        System.err.println("Error: " + "<"  + message + ">");
    }

    /**
     * Logs the message from the exception.
     * @param e The exception to log.
     */
    public static void log(Exception e){
        log(e.getMessage());
    }
}



