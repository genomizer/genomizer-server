package command;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command for changing user.
 *
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
        } catch (SQLException | IOException e) {
            Debug.log("CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " +
                    e.getMessage());
            return new ErrorResponse(StatusCode.BAD_REQUEST,
                    "CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " + e.getMessage());
        }

		String hash = BCrypt.hashpw(password,BCrypt.gensalt());

        try {
            db.resetPassword(username, hash);
        } catch (SQLException | IOException e) {
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Database error " + e.getMessage());
        }

        return new MinimalResponse(StatusCode.CREATED);
    }
}
