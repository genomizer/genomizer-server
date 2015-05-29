package command.user;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by dv13thg on 5/29/15.
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
    }

    @Override
    public Response execute() {

        try(DatabaseAccessor db = initDB()){

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
