package command;

import authentication.PasswordHash;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by dv13jen on 2015-04-16.
 */
public class ChangeUserPasswordCommand extends Command {
    
    @Expose
    private String username = null;

    @Expose
    private String password = null;


    /**
     * Used to validate the ChangeUserPasswordCommand.
     */
    @Override
    public void validate() throws ValidateException {
        validateName(username, MaxLength.USERNAME, "Username/Password");
        validateName(password, MaxLength.PASSWORD, "Username/Password");
    }

    /**
     * Used to execute the actual password change of the user.
     */
    @Override
    public Response execute() {
        DatabaseAccessor db = null;
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
