package api.commands.File;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.UpdateFileRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for updating a file.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutFileTest extends SuperTestCommand {

    private String expected;
    private FileTuple ft;

    /**
     * Defines the fileTuple to update and the expected String to find.
     * @param ident
     * @param ft
     * @param expected
     * @param expectedResult
     */
    public PutFileTest(String ident, FileTuple ft, String expected, boolean expectedResult) {
        super(ident,expectedResult);
        this.expected = expected;
        this.ft = ft;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new UpdateFileRequest(ft.getId()), CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = CommandTester.conn.getResponseBody().contains(expected);
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
