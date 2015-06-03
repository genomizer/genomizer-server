package api.commands;

import api.commands.Admin.DeleteAdminUserTest;
import api.commands.Admin.GetUserList;
import api.commands.Admin.PostAdminUserTest;
import api.commands.Admin.PutAdminUserTest;
import api.commands.Login.LoginTest;
import api.commands.Login.LogoutTest;
import model.Debug;
import util.Constants;

/**
 * Class which contains all the tests for the admin commands.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AdminTests extends TestCollection{

    /**
     * Creates all the admin commands.
     */
    public AdminTests(){
        super();

        super.commandList.add(new PostAdminUserTest("POST ADMIN USER", "tempuser1","pass1", "USER", "name", "mail_no@gmail.com", true));
        super.commandList.add(new GetUserList("GET USER LIST","tempuser1", true));
        super.commandList.add(new PutAdminUserTest("PUT ADMIN USER", "tempuser1", "pass2","GUEST","name2", "new-mail_no@gmail.com", true));
        super.commandList.add(new DeleteAdminUserTest("DELETE CREATED USER","tempuser1", true));
        super.commandList.add(new GetUserList("GET USER LIST","tempuser1", false));

        super.commandList.add(new PostAdminUserTest("POST NEW USER", "tempuser1","pass1", "USER", "name", "mail_no@gmail.com", true));
        super.commandList.add(new PostAdminUserTest("POST DUPLICATE USER", "tempuser1","pass1", "USER", "name", "mail_no@gmail.com", false));
        super.commandList.add(new DeleteAdminUserTest("DELETE NEW USER","tempuser1", true));

        super.commandList.add(new DeleteAdminUserTest("DELETE NONEXISTING USER","tempuser1", false));
        super.commandList.add(new PutAdminUserTest("PUT NONEXISTING USER", "tempuser2", "pass2","GUEST","name2", "new-mail_no@gmail.com", false));

        super.commandList.add(new PostAdminUserTest("POST ADMIN USER", "tempuser1","pass1", "ADMIN", "name", "mail_no@gmail.com", true));
        super.commandList.add(new LogoutTest("LOG OUT STANDARD USER", true));
        super.commandList.add(new LoginTest("LOG IN NEW USER", "tempuser1", "pass1", true));
        super.commandList.add(new PutAdminUserTest("LOWER ADMIN USER", "tempuser1", "pass1","GUEST","name", "mail_no@gmail.com", false));
        super.commandList.add(new PutAdminUserTest("ADMIN TO ADMIN", "tempuser1", "pass1","ADMIN","name", "mail_no@gmail.com", true));
        super.commandList.add(new LogoutTest("LOG OUT NEW USER", true));
        super.commandList.add(new LoginTest("LOG IN STANDARD USER", Constants.userName, Constants.password, true));
        super.commandList.add(new DeleteAdminUserTest("CLEANUP","tempuser1", true));
    }

    @Override
    public boolean execute() {
        System.out.println("\n----------------------ADMIN----------------------");
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
