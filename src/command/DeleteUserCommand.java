package command;

import java.io.IOException;
import java.sql.SQLException;
import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a delete user command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteUserCommand extends Command {
	public String username;

	/**
	 * Constructs a new instance of DeleteUserCommand using the supplied
	 * username.
	 * @param username the username to delete.
	 */
	public DeleteUserCommand(String username) {
		this.username = username;
	}

	@Override
	public void validate() throws ValidateException {
		if(username == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Username was missing.");

		} else if(username.length() < 1 || username.length() >
				database.constants.MaxSize.USERNAME) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Username " +
					"has to be between 1 and " +
					database.constants.MaxSize.USERNAME + " characters long.");

		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db;
		System.out.println("DELETING USER: " + username);
		try {
			db = initDB();
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
					"intiating daabaseaccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}
		try {
			db.deleteUser(username);
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when " +
					"removing user from database, user probably don't exists. "
					+ e.getMessage());
		}
		return new MinimalResponse(StatusCode.OK);
	}
}
