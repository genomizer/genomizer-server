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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to alter user information.
 *
 * @author Business Logic 2015
 * @version 1.1
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
    
    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String username, UserType userType) {
        this.userType = userType;
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(username, MaxLength.USERNAME, "User");
        validateName(password, MaxLength.PASSWORD, "Password");
        validateName(privileges, MaxLength.ROLE, "Privileges");
        validateExists(name, MaxLength.FULLNAME, "Name");
        validateExists(email, MaxLength.EMAIL, "Email");

    }

    @Override
    public Response execute() {
        DatabaseAccessor db = null;
        Response response;

        try {
            db = initDB();
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            if (db.updateUser(username, hash, UserType.valueOf(privileges),
                    name, email) != 0)
                response = new MinimalResponse(HttpStatusCode.OK);
            else
                response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                        "Editing of user '" + username + "' unsuccessful, " +
                                "user does not exist.");
        } catch (SQLException | IOException e) {
            response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Editing of user '" + username + "' unsuccessful due to " +
                            "temporary database problems.");
            Debug.log("Reason :" + e.getMessage());
        } finally {
            if (db != null)
                db.close();
        }

        return response;
    }
}