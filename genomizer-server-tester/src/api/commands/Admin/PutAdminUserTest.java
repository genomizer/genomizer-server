package api.commands.Admin;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.UpdateUserRequest;
import util.Constants;
import util.RequestException;

/**
 * Test for updating a user from the admin command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class PutAdminUserTest extends SuperTestCommand {

    private String username;
    private String password;
    private String privileges;
    private String name;
    private String email;

    /**
     * Defines the user and information to update.
     * @param ident
     * @param username
     * @param password
     * @param privileges
     * @param name
     * @param email
     * @param expectedResult
     */
    public PutAdminUserTest(String ident, String username, String password,
                            String privileges, String name, String email, boolean expectedResult){
        super(ident, expectedResult);
        this.username = username;
        this.password = password;
        this.privileges = privileges;
        this.name = name;
        this.email = email;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(
                    new UpdateUserRequest(username,password,privileges,name,email),
                    CommandTester.token, Constants.JSON);

            if (CommandTester.conn.getResponseCode() == 200){
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
