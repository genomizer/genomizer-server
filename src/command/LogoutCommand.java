package command;

import authentication.Authenticate;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
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

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		this.username = uuid;
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
