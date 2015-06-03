package api.commands.Annotation;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.GetAnnotationRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for getting the annotations.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetAnnotationTest extends SuperTestCommand {

    private String expected;

    /**
     * Sets the expected String to find.
     * @param ident
     * @param expected
     * @param expectedResult
     */
    public GetAnnotationTest(String ident, String expected, boolean expectedResult) {
        super(ident, expectedResult);
        this.expected = expected;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new GetAnnotationRequest(),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = CommandTester.conn.getResponseBody().contains(expected);
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
