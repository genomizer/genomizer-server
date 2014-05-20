package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a delete user command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteUserCommand extends Command {

	/**
	 * Used to validate DeleteUserCommand.
	 */

	public String username;

	public DeleteUserCommand(String restful) {
		username=restful;
	}

	@Override
	public boolean validate() {

		if(username == null) {
			return false;
		}
		// TODO Auto-generated method stub
		return true;

	}

	/**
	 * Used to execute the actual removal of the user.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		System.out.println("DELETING USER: " + username);
		try {
			db = initDB();
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when intiating daabaseaccessor. " + e.getMessage());
		} catch (IOException e)  {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		}
		try {
			db.deleteUser(username);
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Error when removing user from database, user probably don't exists. " + e.getMessage());
		}
		return new MinimalResponse(StatusCode.OK);

	}

}
