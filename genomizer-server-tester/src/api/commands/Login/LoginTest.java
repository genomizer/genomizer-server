package api.commands.Login;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.LoginRequest;
import responses.LoginResponse;
import responses.ResponseParser;
import util.RequestException;

/**
 * Test for the log in command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class LoginTest extends SuperTestCommand {

    private String username;
    private String password;

    /**
     * Sets the username and password for the log in.
     * @param ident
     * @param username
     * @param password
     * @param expectedResult
     */
    public LoginTest(String ident, String username, String password, boolean expectedResult) {
        super(ident, expectedResult);
        this.username = username;
        this.password = password;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new LoginRequest(this.username, this.password), "", "application/json");

            LoginResponse loginResponse = ResponseParser.parseLoginResponse(CommandTester.conn.getResponseBody());
            if (loginResponse != null) {
                CommandTester.token = loginResponse.token;
                super.finalResult = true;
            }
        } catch (RequestException e) {
            if (super.expectedResult){
                ErrorLogger.log(e);
            }
        }
    }
}
