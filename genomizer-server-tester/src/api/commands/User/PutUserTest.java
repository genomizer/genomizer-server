package api.commands.User;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.ChangePasswordRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for updating a user.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutUserTest extends SuperTestCommand {

    private String oldPass;
    private String newPass;
    private String name;
    private String email;

    /**
     * Defines the user and the information to update.
     * @param ident
     * @param oldPass
     * @param newPass
     * @param name
     * @param email
     * @param expectedResult
     */
    public PutUserTest(String ident, String oldPass, String newPass, String name, String email,
                            boolean expectedResult){
        super(ident, expectedResult);
        this.oldPass = oldPass;
        this.newPass = newPass;
        this.name = name;
        this.email = email;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new ChangePasswordRequest(oldPass,
                    newPass, name, email), CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200){
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
