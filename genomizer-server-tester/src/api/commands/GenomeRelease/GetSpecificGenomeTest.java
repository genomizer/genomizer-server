package api.commands.GenomeRelease;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.GetGenomeSpecieReleasesRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for getting a specific genome release.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetSpecificGenomeTest extends SuperTestCommand {

    private String specie;
    private String [] expected;

    /**
     * Defines the genome release to get and the expected String to find in the
     * response.
     * @param ident
     * @param specie
     * @param expected
     * @param expectedResult
     */
    public GetSpecificGenomeTest(String ident, String specie, String [] expected, boolean expectedResult) {
        super(ident, expectedResult);
        this.specie = specie;
        this.expected = expected;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new GetGenomeSpecieReleasesRequest(specie),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                String respBody = CommandTester.conn.getResponseBody();
                super.finalResult = true;
                for (String expectedStr : expected)
                  super.finalResult = (super.finalResult && respBody.contains(expectedStr));
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
