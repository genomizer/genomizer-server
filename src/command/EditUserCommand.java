package command;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin command to change user information
 * Created by dv13jen on 2015-05-04.
 */
public class EditUserCommand extends Command {

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
    public void setFields(String uri, String uuid, UserMethods.UserType userType) {
        this.userType = userType;
		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
    }

    /**
     * Used to make sure the strings of the command are correct
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {

        validateName(username, MaxLength.USERNAME, "User");
        validateName(password, MaxLength.PASSWORD, "Password");
        validateName(privileges, MaxLength.ROLE, "Privileges");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");

    }

    @Override
    public Response execute() {

        DatabaseAccessor db;
        try {
            db = initDB();
        } catch (SQLException | IOException e) {
            Debug.log("UPDATE WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
                    e.getMessage());
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
                    "initiating databaseAccessor. " + e.getMessage());
        }

        try {

            String hash = BCrypt.hashpw(password, BCrypt.gensalt());

            db.updateUser(username, hash, privileges, name, email);

        } catch (SQLException | IOException e) {
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
                    "editing user "+username+" in database, "+"" +
                    "user probably don't exist. " + e.getMessage());
        }

        return new MinimalResponse(StatusCode.OK);

    }
}

