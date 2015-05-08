package command;

import java.io.IOException;
import java.sql.SQLException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to represent a delete user command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteUserCommand extends Command {
	public String username;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		username = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		if(username == null) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,
					"Username was missing.");
		} else if(username.length() < 1 || username.length() >
				MaxLength.USERNAME) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Username " +
					"has to be between 1 and " +
					MaxLength.USERNAME + " characters long.");
		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db;
		System.out.println("DELETING USER: " + username);
		try {
			db = initDB();
		} catch (SQLException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Error when " +
					"intiating daabaseaccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		}
		try {
			db.deleteUser(username);
		} catch (SQLException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Error when " +
					"removing user from database, user probably don't exists. "
					+ e.getMessage());
		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
