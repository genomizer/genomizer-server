package api.commands;

import api.commands.Experiment.DeleteExperimentTest;
import api.commands.File.ChangeIndex;
import api.commands.File.DeleteFileTest;
import api.commands.GenomeRelease.DeleteGenomeTest;
import api.commands.Login.LoginTest;
import api.commands.Login.LogoutTest;
import communication.Connection;
import model.Debug;
import util.Constants;
import util.FileIndices;
import util.StringContainer;


/**
 * Class containing all the testing classes. Should handle all the setup and
 * tear down for the testing. Also contains a few constants which are referenced
 *  in the test classes.
 */
public class CommandTester {
    public static String token =  "";
    private String role;
    public static Connection conn;

    public static StringContainer fileID;

    public static String EXP_NAME = "testExp1";

    /**
     * Runs all the different kinds of commands defined in the test classes.
     * @param connection The established connection to send the commands to.
     */
    public CommandTester (Connection connection) throws Exception {
        for(int i = 0; i < TestCollection.laps; i++){
            conn = connection;

            LoginTests lt = new LoginTests();
            lt.execute();

            new LoginTest("POST LOGIN", Constants.userName, Constants.password, true).execute();

            //GENOME HANDLING (POST, PUT, DELETE, GET)
            GenomeReleaseTests g = new GenomeReleaseTests();
            g.execute();

            //Test experiments (POST, GET, PUT, DELETE)
            ExperimentTests e = new ExperimentTests();
            e.execute();

            //Test FILE (POST, GET, DELETE)
            FileTests ft = new FileTests();
            ft.execute();

            //CONVERT (PUT)
            ConvertTests ct = new ConvertTests();
            ct.execute();

            //SEARCH (GET)
            SearchTests st = new SearchTests();
            st.execute();

            //USER
            UserTests ut = new UserTests();
            ut.execute();

            //ADMIN (POST, PUT, DELETE)
            AdminTests at = new AdminTests();
            at.execute();

            //PROCESSING
//            ProcessingTests pt = new ProcessingTests();


            //ANNOTATION (POST, PUT, DELETE, GET)
            AnnotationTests a = new AnnotationTests();
            a.execute();

            //Clean up for the tests.
            new DeleteGenomeTest("CLEANUP", "Human", "MultiFileTest", true).execute();
            while(FileIndices.getSize() > 0) {
                ChangeIndex ci = new ChangeIndex("CHANGE INDEX", CommandTester.EXP_NAME, 0, -1, true);
                ci.execute();
                DeleteFileTest df = new DeleteFileTest("DELETE FILE", null, false);
                df.execute();
            }
            DeleteExperimentTest de = new DeleteExperimentTest("FINAL CLEANUP", CommandTester.EXP_NAME, true);
            de.execute();

            Debug.log(de.toString(), de.finalResult == de.expectedResult);

            new LogoutTest("DELETE LOGIN", true).execute();


        }

        //Prints the test result in a nice format.
        System.out.println("\n-------------------------------------------------");
        System.out.println("Total tests run: " + TestCollection.runTests);
        System.out.println("Successful tests: " + TestCollection.succeededTests);
        System.out.println("Failed tests: " + TestCollection.failedTests);
        System.out.println("-------------------------------------------------");
        System.out.println("Failed:\n");
        for(String s : TestCollection.nameOfFailedTests){
            System.out.println(s);
        }
        System.out.println("-------------------------------------------------");
    }
}
