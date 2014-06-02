package command;

import authentication.Authenticate;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class LogoutCommand extends Command {

	private String username;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param username that wants to logout.
	 */
	public LogoutCommand(String username) {

		this.username = username;

	}

	/**
	 * Used to validate the LogoutCommand class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(username == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Username was missing.");

		} else if(username.length() < 1 || username.length() > database.constants.MaxSize.USERNAME) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Username has to be between 1 and "
					+ database.constants.MaxSize.USERNAME + " characters long.");

		}

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
