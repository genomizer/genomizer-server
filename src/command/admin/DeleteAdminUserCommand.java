package command.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

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
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {
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
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			if (db.deleteUser(username) != 0)
				response = new MinimalResponse(HttpStatusCode.OK);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of user '" + username + "' unsuccessful, " +
								"user does not exist.");
		} catch (SQLException | IOException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of user '" + username + "' unsuccessful due to " +
							"temporary problems with the database.");
			Debug.log("Reason: " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}