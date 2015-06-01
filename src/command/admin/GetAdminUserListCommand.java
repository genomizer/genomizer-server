package command.admin;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Command used to get a list of users.
 *
 * @author Business Logic 2015
 * @version 1.0
 */
public class GetAdminUserListCommand extends Command{
    @Override
    public int getExpectedNumberOfURIFields() { return 2; }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
    }

    @Override
    public Response execute() {
        List<String> usernameArr;

        try (DatabaseAccessor db = initDB()){
            usernameArr = db.getUsers();

        } catch (SQLException e) {
            Debug.log("Reason: " + e.getMessage());
            return new DatabaseErrorResponse("Retrieving list of users");
        } catch (IOException e) {
            Debug.log("Reason: " + e.getMessage());
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Retrieving list of users"+ e.getMessage());
        }
        if(usernameArr == null){
            return new ErrorResponse(HttpStatusCode.NOT_FOUND,"Could not find any users");
        }
        return new UserListResponse(usernameArr);
    }
}
