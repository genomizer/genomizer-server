package command.user;

import authentication.Authenticate;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Command used to retrieve user information.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class GetUserCommand extends Command {
    private String username;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 2;
    }

    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String uuid, UserMethods.UserType userType) {
        super.setFields(uri, query, uuid, userType);
        username = uri.split("/")[2];
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));

        /*If the user is not admin he/she can only retrieve information
        * about him/herself.*/
        if (this.userType != UserMethods.UserType.ADMIN && !Authenticate.
                getUsernameByID(uuid).equals(username))
                throw new ValidateException(HttpStatusCode.UNAUTHORIZED,
                        "User does not har rights");
    }

    @Override
    public Response execute() {
        Response response;
        try(DatabaseAccessor db = initDB()){
            List<String> userinfo = new ArrayList<>();
            userinfo.add(username);
            userinfo.add(db.getRole(username).name());
            userinfo.add(db.getUserFullName(username));
            userinfo.add(db.getUserEmail(username));
            if (userinfo.size() != 4)
                response = new ErrorResponse(HttpStatusCode.NOT_FOUND,
                        "Retrieval of user '" + username +
                                "' unsuccessful. All information not listed.");
            else
                response = new UserInfoResponse(userinfo);
        } catch (SQLException e) {
            Debug.log("SQLException :  " + e.getMessage());
            response = new DatabaseErrorResponse("Retrieval of user '"
                    + username + "'");
        } catch (IOException e) {
            Debug.log("IOException" + e.getMessage());
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Retrieval of user '" + username + "' unsuccessful. " +
                            e.getMessage());
        }

        return response;
    }
}
