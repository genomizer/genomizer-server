package command.user;

import authentication.BCrypt;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command class which enables the user to change their password.
 *
 * @author dv13jen
 * @version 1.0
 */
public class PutUserPasswordCommand extends Command {
    
    @Expose
    private String username = null;

    @Expose
    private String password = null;

    @Override
    public void setFields(String uri, String uuid, UserType userType) {

        super.setFields(uuid, userType);
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

        DatabaseAccessor db = null;

        try {
            db = initDB();
        } catch (SQLException | IOException e) {
            Debug.log("CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " +
                    e.getMessage());
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "CHANGE OF PASSWORD FAILED FOR: " + username + ". REASON: " + e.getMessage());
        }

		String hash = BCrypt.hashpw(password,BCrypt.gensalt());

        try {
            db.resetPassword(username, hash);
        } catch (SQLException | IOException e) {
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Database error " + e.getMessage());
        }

        return new MinimalResponse(HttpStatusCode.CREATED);
    }
}
