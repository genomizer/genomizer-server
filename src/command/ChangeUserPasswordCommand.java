package command;

import authentication.PasswordHash;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command class which enables the user to change their password.
 *
 * @author dv13jen
 * @version 1.0
 */
public class ChangeUserPasswordCommand extends Command {
    
    @Expose
    private String username = null;

    @Expose
    private String password = null;

    @Override
    public void setFields(String uri, String uuid, UserType userType) {
        this.userType = userType;

        /*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
    }

    /**
     * Used to validate the ChangeUserPasswordCommand.
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateString(username, MaxLength.USERNAME, "Username/Password");
        validateString(password, MaxLength.PASSWORD, "Username/Password");
    }

    /**
     * Used to execute the actual password change of the user.
     */
    @Override
    public Response execute() {
        DatabaseAccessor db;
        try {
            db = initDB();
        } catch (SQLException | IOException e) {
            Debug.log("CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " +
                    e.getMessage());
            return new ErrorResponse(StatusCode.BAD_REQUEST,
                    "CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " + e.getMessage());
        }

		String salt = PasswordHash.getNewSalt();
		// get hash using salt and password
		String hash = PasswordHash.hashString(password+salt);
		// insert into DB, requires method DB group
        try {
            db.resetPassword(username, hash, salt);
        } catch (SQLException | IOException e) {
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Database error " + e.getMessage());
        }

        return new MinimalResponse(StatusCode.CREATED);
    }
}
