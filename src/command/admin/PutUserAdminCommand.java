package command.admin;

import database.subClasses.UserMethods.UserType;
import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;
import server.Debug;
import database.subClasses.UserMethods;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin command to change user information
 * Created by dv13jen on 2015-05-04.
 */
public class PutUserAdminCommand extends Command {

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
     * Set the UserType. Uri and Uuid not used in this command.
     * @param uri the URI from the http request.
     * @param query
     * @param uuid
     * @param userType
     */
    @Override
    public void setFields(String uri, String query, String uuid, UserMethods.UserType userType) {
        this.userType = userType;
		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
    }

    /**
     * Used to make sure the strings of the command are correct
     * @throws command.ValidateException
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
     * Perform the action of the command and return a response.
     * A connection to the database is established and the user
     * is updated with the information given.
     * @return a response of the result of the command.
     */
    @Override
    public Response execute() {

        DatabaseAccessor db;
        try {
            db = initDB();
        } catch (SQLException | IOException e) {
            Debug.log("Editing of user: " + username + " didn't work, reason: " +
                    e.getMessage());
            return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Editing of user: " + username +
                    " didn't work because of temporary problems with database.");
        }
        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            db.updateUser(username, hash, UserType.valueOf(privileges), name, email);
        } catch (SQLException | IOException e) {
            Debug.log("Editing of user: " + username + " didn't work, reason: " +
                    e.getMessage());
            return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Error when " +
                    "editing user "+username+" in database, "+"" +
                    "user probably don't exist. ");
        }finally {
            db.close();

        }
        return new MinimalResponse(HttpStatusCode.OK);

    }
}