package model;

/**
 * Class used for printing the test results.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class Debug {

    /**
     * Logs the test result in a nice format.
     * @param message The test to log.
     * @param status If the test succeeded or not.
     */
    public static void log(String message, boolean status) {
        if(status){
            System.out.flush();
            System.out.printf("TEST: %-30.30s  STATUS: %-30.30s%n",message, "SUCCESS");
            System.out.flush();
        }else{
            System.err.flush();
            System.err.printf("TEST: %-30.30s  STATUS: %-30.30s%n",message, "FAIL");
            System.err.flush();
        }
    }
}
