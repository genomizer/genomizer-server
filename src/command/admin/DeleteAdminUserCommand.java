package command.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import authentication.Authenticate;
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
		username = uri.split("/")[3];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(username, MaxLength.USERNAME, "Username");
	}

	@Override
	public Response execute() {
		if (Authenticate.getUsernameByID(uuid).equals(username)) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of user '" + username +
							" unsuccessful. Deletion of admins own account " +
							"is not allowed. Use another admin user " +
							"to delete the account.");
		} 

		try (DatabaseAccessor db = initDB()) {
			if (db.deleteUser(username) != 0) {
				Authenticate.deleteUsername(username);
				return new MinimalResponse(HttpStatusCode.OK);
			} else {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of user '" + username +
								"' unsuccessful, user does not exist.");
			}
		} catch (SQLException e) {
			Debug.log("DeleteAdminUserCommand.execute: SQLException. Reason: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.
					INTERNAL_SERVER_ERROR, "Deletion of user '" + username +
					"' unsuccessful due to temporary database problems.");
		} catch (IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of user '" + username + "' unsuccessful. " +
							e.getMessage());
		}
	}
}
