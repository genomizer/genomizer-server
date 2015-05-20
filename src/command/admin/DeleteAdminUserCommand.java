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
 * Command used to delete a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteAdminUserCommand extends Command {
	private String username;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, String query, String uuid,
						  UserType userType) {
		super.setFields(uri, query, uuid, userType);
		username = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(username, MaxLength.USERNAME, "Username");
	}

	@Override
	public Response execute() {
		Response response;

		try {
			DatabaseAccessor db = initDB();
			if (db.getUsers().contains(username)) {
				db.deleteUser(username);
				response = new MinimalResponse(HttpStatusCode.OK);
			} else {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of user unsuccessful, user does not exist.");
			}

			db.close();
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of user: " + username + " was unsuccessful, " +
					"reason: " + e.getMessage());
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of user: " + username + " was unsuccessful due " +
							"to temporary problems with the database.");
		}

		return response;
	}
}
