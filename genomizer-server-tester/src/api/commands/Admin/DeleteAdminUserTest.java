package api.commands.Admin;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.DeleteUserRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for deleting a user.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class DeleteAdminUserTest extends SuperTestCommand {

    private String username;

    /**
     * Defines the user to delete.
     * @param ident
     * @param username
     * @param expectedResult
     */
    public DeleteAdminUserTest(String ident, String username, boolean expectedResult){
        super(ident, expectedResult);
        this.username = username;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(
                    new DeleteUserRequest(username),
                    CommandTester.token, Constants.TEXT_PLAIN);
            if (CommandTester.conn.getResponseCode() == 200){
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
