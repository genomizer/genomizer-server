package command.admin;

import java.io.IOException;
import java.sql.SQLException;

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

/**
 * Class used to represent a delete user command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteUserCommand extends Command {
	public String username;


	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, String query, String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
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
		try {
			db = initDB();
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of user: " + username + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Deletion of user: " + username +
					" didn't work because of temporary problems with database.");
		}
		try {
			db.deleteUser(username);
		} catch (SQLException e) {
			Debug.log("Deletion of user: " + username + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Error when " +
					"removing user from database, user probably don't exists. ");
		}finally {
			db.close();

		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
