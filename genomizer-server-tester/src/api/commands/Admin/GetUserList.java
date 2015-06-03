package api.commands.Admin;

import api.commands.CommandTester;
import api.commands.SuperTestCommand;
import model.ErrorLogger;
import requests.GetUserNamesRequest;
import responses.ResponseParser;
import util.Constants;
import util.RequestException;
import util.UserList;

/**
 * Test for getting the user list command.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class GetUserList extends SuperTestCommand{

    public static UserList usernames = new UserList();
    private String expected;

    /**
     * Defines the expected String to find in the response.
     * @param ident
     * @param expected
     * @param expectedResult
     */
    public GetUserList(String ident, String expected, boolean expectedResult) {
        super(ident, expectedResult);
        this.expected = expected;
    }

    @Override
    public void execute() {
        try {
            CommandTester.conn.sendRequest(new GetUserNamesRequest(),
                    CommandTester.token, Constants.TEXT_PLAIN);

            if (CommandTester.conn.getResponseCode() == 200) {
                usernames = ResponseParser.parseUserList(CommandTester.conn.getResponseBody());

                super.finalResult = true;

                if (!expected.equals(""))
                    super.finalResult = usernames.userExist(expected);
            }

        } catch (RequestException e) {
            if (super.expectedResult) ErrorLogger.log(e);
        }
    }
}
