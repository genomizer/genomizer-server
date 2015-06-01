package command.user;

import authentication.Authenticate;
import authentication.BCrypt;
import authentication.LoginAttempt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.*;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to alter user information.
 * @author Business Logic 2015
 * @version 1.1
 */
public class PutUserCommand extends Command {
    private String username = null;
    @Expose
    private String oldPassword = null;
    @Expose
    private String newPassword = null;
    @Expose
    private String name = null;
    @Expose
    private String email = null;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String uuid, UserType userType) {
        username = Authenticate.getUsernameByID(uuid);
        super.setFields(uri, query, uuid, userType);
    }

    /**
     * Set the username of the user to edit. Only used during testing
     *
     * @param username - the username of the user to edit.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));

        validateName(oldPassword, MaxLength.PASSWORD, "oldPassword");
        validateName(newPassword, MaxLength.PASSWORD, "newPassword");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");
    }

    @Override
    public Response execute() {
        Response response;
        try (DatabaseAccessor db = initDB()) {
            String dbHash = db.getPasswordHash(username);
            if (dbHash == null || dbHash.isEmpty()) {
                response = new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
                        "Editing of '" + username + "' unsuccessful, " +
                                "username incorrect.");
            } else {
                LoginAttempt login = Authenticate.login(username, oldPassword,
                        dbHash);
                if (login.wasSuccessful()) {
                    db.updateUser(username, BCrypt.hashpw(newPassword,
                            BCrypt.gensalt()), name, email);
                    response = new MinimalResponse(HttpStatusCode.OK);
                } else {
                    response = new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
                            "Editing of user'" + username + "' unsuccessful, " +
                                    "incorrect old password");
                }
            }
        } catch (SQLException e) {
            response = new DatabaseErrorResponse("Editing of user '" +
                    username + "'");
            Debug.log("Reason: " + e.getMessage());
        } catch (IOException e) {
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Editing of user '" + username + "' unsuccessful. " +
                            e.getMessage());
        }

        return response;
    }
}