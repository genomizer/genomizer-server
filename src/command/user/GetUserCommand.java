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
 * Retrieves the information about a user from the db.
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
    public void setFields(String uri, HashMap<String, String> query, String uuid, UserMethods.UserType userType) {
        super.setFields(uri, query, uuid, userType);
        username = uri.split("/")[2];
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateExists(username, MaxLength.USERNAME,"Username");
        /**
         * if user is not admin it can only retrieve information
         * about themselves
         */
        if(this.userType != UserMethods.UserType.ADMIN)
            if(!Authenticate.getUsernameByID(uuid).equals(username))
                throw new ValidateException(HttpStatusCode.UNAUTHORIZED,"User does not har rights");
    }

    @Override
    public Response execute() {
        List<String> userinfo = new ArrayList<>();
        try(DatabaseAccessor db = initDB()){
            userinfo.add(username);
            userinfo.add(db.getRole(username).name());
            userinfo.add(db.getUserFullName(username));
            userinfo.add(db.getUserEmail(username));
        } catch (SQLException e) {
            Debug.log("SQLException :  " + e.getMessage());
            return new DatabaseErrorResponse("Retrieval of User " + username + " ");
        } catch (IOException e) {
            Debug.log("IOException" + e.getMessage());
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Retrieval of user "+username+ " "+ e.getMessage());
        }

        if(userinfo.size() != 4){
            return new ErrorResponse(HttpStatusCode.NOT_FOUND, "Could not find all info about user");
        }
        return new UserInfoResponse(userinfo);
    }
}
