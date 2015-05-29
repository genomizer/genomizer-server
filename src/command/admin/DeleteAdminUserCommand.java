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

	/**
	 * Get the username to be deleted from the uri
	 * @param uri isn't used. Override it to use it.
	 * @param query
	 * @param uuid the UUID for the user who made the request.
	 * @param userType the user type for the command caller.
	 */
	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {
		super.setFields(uri, query, uuid, userType);
		username = uri.split("/")[3];
	}

	/**
	 * Make sure that an admin is using the command and check that username is properly formated.
	 * @throws ValidateException
	 */
	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(username, MaxLength.USERNAME, "Username");
	}

	/**
	 * Try deletion of the inputed user, return a response of the result.
	 * @return
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		// Do not allow deletion of admins own account. This make sure the existence of at least one admin account.
		if(Authenticate.getUsernameByID(uuid).equals(username)){
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Deletion of user "+username+" is not allowed. " +
					"Deletion of admins own account is not allowed. Use another admin user to delete this account.");
		}

		try {
			db = initDB();
			if (db.deleteUser(username) != 0) {
				response = new MinimalResponse(HttpStatusCode.OK);
				Authenticate.deleteUsername(username);
			}
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of user '" + username + "' unsuccessful, " +
								"user does not exist.");
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of user '" + username + "' unsuccessful due to " +
							"temporary database problems.");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of user '" + username + "' unsuccessful. " +
							e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
