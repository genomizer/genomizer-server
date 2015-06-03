package api.commands.GenomeRelease;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.GetGenomeReleasesRequest;
import util.Constants;
import util.RequestException;

/**
 * Command to get all the genome releases.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetAllGenomeTest extends SuperTestCommand {
    private String expected;

    /**
     * Sets the expected String to find in the get.
     * @param ident
     * @param expected
     * @param expectedResult
     */
    public GetAllGenomeTest(String ident, String expected, boolean expectedResult) {

        super(ident, expectedResult);
        this.expected = expected;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new GetGenomeReleasesRequest(),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = CommandTester.conn.getResponseBody().contains(expected);
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}

