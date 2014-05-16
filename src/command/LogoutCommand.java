package command;

import authentication.Authenticate;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class LogoutCommand extends Command {

	private String username;

	public LogoutCommand(String username) {

		this.username = username;

	}

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {
		String id = Authenticate.getID(username);
		if(Authenticate.idExists(id)) {
			Authenticate.deleteUser(id);
			return new MinimalResponse(StatusCode.OK);
		} else {
			return 	new ErrorResponse(StatusCode.FILE_NOT_FOUND, "User not found");
		}
	}

}
