package api.commands.Login;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.LogoutRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for the logout command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class LogoutTest extends SuperTestCommand{

    /**
     * Creates the logout command.
     * @param ident
     * @param expectedResult
     */
    public LogoutTest(String ident, boolean expectedResult) {
        super(ident, expectedResult);
      }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new LogoutRequest(), CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
