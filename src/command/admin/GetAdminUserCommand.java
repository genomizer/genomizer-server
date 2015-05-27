package command.admin;

import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.UserListResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Command used to get a list of users.
 */
public class GetAdminUserCommand extends Command{


    @Override
    public int getExpectedNumberOfURIFields() { return 2; }

    @Override
    public void validate() throws ValidateException {}

    @Override
    public Response execute() {
        List<String> usernameArr = null;

        try (DatabaseAccessor db = initDB()){
            usernameArr = db.getUsers();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(usernameArr == null){
            return new ErrorResponse(HttpStatusCode.NOT_FOUND,"Could not find any users");
        }
        return new UserListResponse(usernameArr);
    }
}
