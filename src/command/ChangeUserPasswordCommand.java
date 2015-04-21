package command;

import authentication.PasswordHash;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

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
    public boolean validate() {

        if(username == null || password == null ) {
            return false;
        }
        if(username.length() < 1 || username.length() > MaxSize.USERNAME) {
            return false;
        }
        if(password.length() < 1 || password.length() > MaxSize.PASSWORD) {
            return false;
        }

        if(username.indexOf('/') != -1) {
            return false;
        }

        return true;
    }

    /**
     * Used to execute the actual password change of the user.
     */
    @Override
    public Response execute() {
        DatabaseAccessor db = null;
        try {
            db = initDB();
        } catch (SQLException e) {
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
                    "initiating databaseAccessor. " + e.getMessage());
        } catch (IOException e)  {
            return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
        }

		String salt = PasswordHash.getNewSalt();
		// get hash using salt and password
		String hash = PasswordHash.hashString(password+salt);
		// insert into DB, requires method DB group
        //db.changeUserPassword(username, salt, hash);

        return new MinimalResponse(StatusCode.CREATED);
    }
}
