package command;

import authentication.Authenticate;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class LogoutCommand extends Command {
	private String username;

	/**
	 * Constructs a new instance of LogoutCommand using the supplied
	 * username.
	 * @param username the username of the user that should be logged out.
	 */
	public LogoutCommand(String username) {
		this.username = username;
	}

	@Override
	public void validate() throws ValidateException {
		validateString(username, MaxLength.USERNAME, "Username");
	}

	@Override
	public Response execute() {
		String id = Authenticate.getID(username);
		if(Authenticate.idExists(id)) {
			Authenticate.deleteActiveUser(id);
			return new MinimalResponse(StatusCode.OK);
		} else {
			return 	new ErrorResponse(StatusCode.NOT_FOUND,
					"User not found");
		}
	}

}
