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
        Response response;

        try (DatabaseAccessor db = initDB()) {
            List<String> usernameArr;
            if ((usernameArr = db.getUsers()) != null)
                response = new UserListResponse(usernameArr);
            else
                response = new ErrorResponse(HttpStatusCode.NOT_FOUND,
                        "Retrieval of user list unsuccessful, no users exist");
        } catch (SQLException e) {
            response = new DatabaseErrorResponse("Retrieval of user list");
            Debug.log("Reason: " + e.getMessage());
        } catch (IOException e) {
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Retrieval of user list unsuccessful. " + e.getMessage());
        }

        return response;
    }
}
