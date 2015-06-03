package command.admin;

import authentication.Authenticate;
import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command used to alter user information.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class PutAdminUserCommand extends Command {
    @Expose
    private String username = null;
    @Expose
    private String password = null;
    @Expose
    private String privileges = null;
    @Expose
    private String name = null;
    @Expose
    private String email = null;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 2;
    }


    /**
     * Set the username of the user to edit. Only used during testing
     *
     * @param username - the username of the user to edit.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Make sure rights is correct and input is properly formatted.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(username, MaxLength.USERNAME, "User");
        validateName(password, MaxLength.PASSWORD, "Password");
        validateName(privileges, MaxLength.ROLE, "Privileges");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");

    }

    /**
     * Try admin editing of inputed user. Return a response of the result.
     * @return
     */
    @Override
    public Response execute() {
        Response response;

        // Do not allow admins to lower privileges of their own accounts. Instead PutUserCommand should be used.
        // This ensures admins can not self edit their privileges, potentially destroying the last admin account.
        if(Authenticate.getUsernameByID(uuid).equals(username)) {
            if (!privileges.equals(UserType.ADMIN.name())) {
                return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Changing of privileges on user " + username +
                        " is not allowed. You may not lower your own privileges.");
            }
        }

        try(DatabaseAccessor db = initDB()) {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            if (db.updateUser(username, hash, UserType.valueOf(privileges),
                    name, email) != 0)
                response = new MinimalResponse(HttpStatusCode.OK);
            else
                response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                        "Editing of user '" + username + "' unsuccessful, " +
                                "user does not exist.");
        } catch (SQLException e) {
            response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Editing of user '" + username + "' unsuccessful due to " +
                            "temporary database problems.");
            Debug.log("Reason :" + e.getMessage());
        } catch (IOException e) {
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Editing of user '" + username + "' unsuccessful. " +
                            e.getMessage());
        }

        return response;
    }
}