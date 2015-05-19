package command.user;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command class which enables the user to change their password.
 *
 * @author dv13jen
 * @version 1.0
 */
@Deprecated
public class PutUserPasswordCommand extends Command {
    
    @Expose
    private String username = null;

    @Expose
    private String password = null;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

    /**
     * Used to validate the PutUserPasswordCommand.
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(username, MaxLength.USERNAME, "Username/Password");
        validateName(password, MaxLength.PASSWORD, "Username/Password");
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
            return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Change of password failed for user: "+username+". Temporary problems with database.");
        }

		String hash = BCrypt.hashpw(password,BCrypt.gensalt());

        try {
            db.resetPassword(username, hash);
        } catch (SQLException | IOException e) {
            Debug.log("CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " +
                    e.getMessage());
            return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Change of password failed for user: "+username+". Temporary problems with database.");
        }finally {
            db.close();
        }

        return new MinimalResponse(HttpStatusCode.OK);
    }
}
