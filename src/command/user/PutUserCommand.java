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
import database.subClasses.UserMethods;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command for user to update his/her information
 * Created by dv13jen on 2015-05-04.
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

    /**
     * Set username using uuid along with the usertype
     * @param uri the URI from the http request.
     * @param query the query of the request
     * @param username the uuid from the http request.
     * @param userType the userType
     */
    @Override
    public void setFields(String uri, String query, String username, UserMethods.UserType userType) {
        this.username = username;
        this.userType = userType;
    }

    /**
     * Used to make sure the strings of the command are correct
     * @throws command.ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));

        validateName(oldPassword, MaxLength.PASSWORD, "oldPassword");
        validateName(newPassword, MaxLength.PASSWORD, "newPassword");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");
    }

    /**
     * Perform the action of the command and return a response.
     * A connection to the database is established, authentication is performed
     * and a response is return of the result.
     * @return a response of the result of the command.
     */
    @Override
    public Response execute() {
        DatabaseAccessor db;
        String dbHash;
        try {
            db = initDB();
            dbHash = db.getPasswordHash(username);

        } catch (SQLException | IOException e) {
            Debug.log("Update was unsuccessful for user: " + username + ". Reason: " +
                    e.getMessage());
            return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Update of user information" +
                    " didn't work because of temporary problems with database.");
        }

        if(dbHash == null || dbHash.isEmpty()){
            return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, "Incorrect user name for user "
                    + username);
        }

        LoginAttempt login = Authenticate.login(username, oldPassword, dbHash);

        if(login.wasSuccessful()) {
            try {
                String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                db.updateUser(username, hash, name, email);
            } catch (SQLException | IOException e) {
                Debug.log("Update was unsuccessful for user: " + username + ". Reason: " +
                        e.getMessage());
                return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Update of user information" +
                        " didn't work because of temporary problems with database.");
            }finally {
                db.close();
            }
            return new MinimalResponse(HttpStatusCode.OK);
        }
        Debug.log("Update was unsuccessful for user: " + username + ". Reason: Incorrect old password");
        return new ErrorResponse(HttpStatusCode.UNAUTHORIZED, "Error with update of user information. " +
                "Incorrect old password");
    }
}