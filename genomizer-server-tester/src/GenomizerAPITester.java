import api.commands.CommandTester;
import api.commands.TestCollection;
import communication.Connection;
import communication.ConnectionFactory;
import communication.SSLTool;

import static api.commands.TestCollection.*;

/**
 * Main class for the test program.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GenomizerAPITester {

    public static String token;

    /**
     * MAIN
     */
    public static void main(String[] args) throws Exception {

        // path to server
        if (args.length != 1) {
            System.err.println("Address for server to run on is missing. \n" +
                    "Aborting...");
            System.exit(-1);
        }
        ConnectionFactory cf = new ConnectionFactory();
        cf.setIP(args[0]);

        SSLTool.disableCertificateValidation();
        Connection c = cf.makeConnection();
        new CommandTester(c);
        //Do sequential testing

        System.exit(failedTests);
    }
}
