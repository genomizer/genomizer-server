package api.commands.File;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.DownloadFileRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for getting a file from an experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetFileTest extends SuperTestCommand {

    private String expected;

    /**
     * Define the expected String to find in the response.
     * @param ident
     * @param expected
     * @param expectedResult
     */
    public GetFileTest(String ident, String expected, boolean expectedResult) {
        super(ident, expectedResult);
        this.expected = expected;

    }

    @Override
    public void execute() {

        try {
            CommandTester.conn.sendRequest(new DownloadFileRequest(CommandTester.fileID.getString(), ""),
                    CommandTester.token, Constants.TEXT_PLAIN);
            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = CommandTester.conn.getResponseBody().contains(expected);
            }

        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);

        }


    }
}
