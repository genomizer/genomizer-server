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
    public void setFields(String uri, String query, String uuid,
                          UserType userType) {
        super.setFields(uri, query, uuid, userType);
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
        Response response;

        try {
            DatabaseAccessor db = initDB();
            if (db.getUsers().contains(username)) {
                String hash = BCrypt.hashpw(password, BCrypt.gensalt());
                db.updateUser(username, hash, UserType.valueOf(privileges),
                        name, email);
                response = new MinimalResponse(HttpStatusCode.OK);
            } else {
                response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                        "Editing of user " + username + " was unsuccessful, " +
                                "user does not exist.");
            }

            db.close();
        } catch (SQLException | IOException e) {
            Debug.log("Editing of user: " + username + " was unsuccessful, " +
                    "reason: " + e.getMessage());
            response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "Editing of user: " + username + " was unsuccessful due " +
                            "to temporary problems with the database.");
        }

        return response;
    }
}