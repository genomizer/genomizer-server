package api.commands;


import api.commands.ConvertFile.ConvertFileTest;
import api.commands.ConvertFile.GetConvertedFileTest;
import api.commands.File.*;
import model.Debug;
import util.Constants;
import util.StringContainer;

/**
 * Contains all the tests for the conversion commands.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ConvertTests extends TestCollection{
    public static StringContainer convertedFile;

    /**
     * Defines the test files to use and the commands to test.
     */
    public ConvertTests() {
        super();

        FileTuple ft1 = new FileTuple();
        ft1.setId(CommandTester.EXP_NAME);
        ft1.setName("test.sgr");
        ft1.setAuthor(Constants.userName);
        ft1.setMetaData("default");
        ft1.setType("profile");
        ft1.setGrVersion("MultiFileTest");

        //This reference needs to be set during runtime.
        convertedFile = new StringContainer("");

        super.commandList.add(new PostFileTest("POST CONVERT FILE", ft1, true));
        super.commandList.add(new ChangeIndex("GET FILE TO CONVERT", CommandTester.EXP_NAME,0,-1, true));
        super.commandList.add(new ConvertFileTest("CONVERT FILE", "wig", true));
        super.commandList.add(new GetConvertedFileTest("GET CONVERT FILE", true));
        super.commandList.add(new ChangeIndex("GET FILE TO CONVERT", CommandTester.EXP_NAME,0,-1, true));
        super.commandList.add(new DeleteFileTest("CLEANUP", null, true));
        super.commandList.add(new ChangeIndex("GET FILE TO CONVERT", CommandTester.EXP_NAME,0,-1, true));
        super.commandList.add(new DeleteFileTest("CLEANUP2", null, true));
    }

    @Override
    public boolean execute() {
        System.out.println("\n---------------------CONVERT---------------------");

        boolean bigResult = true;
        for (SuperTestCommand stc : super.commandList) {
            stc.execute();

            if ( stc instanceof ChangeIndex) {
                if (!stc.finalResult) {
                    nameOfFailedTests.add(stc.ident);
                    bigResult = false;
                }
                continue;
            }

            runTests++;

            boolean succeeded = stc.finalResult == stc.expectedResult;

            if (succeeded) {
                succeededTests++;
            } else {
                failedTests++;
                nameOfFailedTests.add(stc.ident);
                bigResult = false;
            }

            Debug.log(stc.toString(), succeeded);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bigResult;
    }
}
