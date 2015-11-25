package api.commands;

import api.commands.Search.SearchTestExpID;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import model.Debug;

/**
 * Class containing the tests for the search commands.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class SearchTests extends TestCollection {

    /**
     * Creates the commands to test.
     */
    public SearchTests() throws UnsupportedEncodingException {
        super();

        super.commandList.add(new SearchTestExpID("SEARCH EXP ID", "Exp1[expID]", "testExp1", true));
        super.commandList.add
          (new SearchTestExpID
           ("SEARCH MULTI",
            URLEncoder.encode("Exp1[ExpID] AND Human[Species]", "UTF-8"),
            "testExp1", true));
        super.commandList.add(new SearchTestExpID("SEARCH INVALID", "OAJHG", "", false));
        super.commandList.add(new SearchTestExpID("SEARCH EMPTY", "", "testExp1", true));
    }

    @Override
    public boolean execute() {
        System.out.println("\n----------------------SEARCH---------------------");
        boolean bigResult = true;
        for (SuperTestCommand stc: super.commandList) {
            stc.execute();
            runTests++;

            boolean succeeded = stc.finalResult == stc.expectedResult;

            if (succeeded){
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
