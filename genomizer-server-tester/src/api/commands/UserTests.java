package api.commands;
import api.commands.Admin.DeleteAdminUserTest;
import api.commands.Admin.PostAdminUserTest;
import api.commands.User.PutUserTest;
import api.commands.Login.LoginTest;
import api.commands.Login.LogoutTest;
import model.Debug;
import util.Constants;

/**
 * Tests for the user commands.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class UserTests extends TestCollection {

    /**
     * Creates the user commands to test.
     */
    public UserTests(){
        super();
        super.commandList.add(new PostAdminUserTest("POST NEW ADMIN USER", "tempuser1","pass1", "ADMIN", "name", "mail_no@gmail.com", true));
        super.commandList.add(new LogoutTest("LOG OUT STANDARD USER", true));
        super.commandList.add(new LoginTest("LOG IN NEW USER", "tempuser1", "pass1", true));
        super.commandList.add(new PutUserTest("UPDATE NEW USER", "pass1", "pass2", "hejhopp", "mailzzzzzz@stuff.com", true));
        super.commandList.add(new PutUserTest("WRONG UPDATE NEW USER", "pass1", "pass3", "hejhopp", "mailzzzzzz@stuff.com", false));
        super.commandList.add(new LogoutTest("LOG OUT NEW USER", true));
        super.commandList.add(new LoginTest("LOG IN UPDATED USER", "tempuser1", "pass2", true));
        super.commandList.add(new LogoutTest("LOG OUT UPDATED USER", true));
        super.commandList.add(new LoginTest("LOG IN STANDARD USER", Constants.userName, Constants.password, true));
        super.commandList.add(new DeleteAdminUserTest("DELETE NEW ADMIN USER", "tempuser1", true));
    }
    @Override
    public boolean execute() {

        System.out.println("\n----------------------USER-----------------------");
        boolean bigResult = true;

        for (SuperTestCommand stc: super.commandList) {
            stc.execute();
            runTests++;
            boolean succeeded = stc.finalResult == stc.expectedResult;

            if (succeeded){
                succeededTests++;
            } else {
                failedTests++;
                nameOfFailedTests.add(stc.ident);
                bigResult = false;
            }

            Debug.log(stc.toString(), succeeded);

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return bigResult;
    }
}
