package command.connection;

import authentication.Authenticate;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Command used to log out a user.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteLoginCommand extends Command {
	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {
		validateName(userName, MaxLength.USERNAME, "Username");
	}

	@Override
	public Response execute() {
		Response response;
		String id = Authenticate.getID(userName);
		if (Authenticate.idExists(id)) {
			Authenticate.deleteActiveUser(id);
			response = new MinimalResponse(HttpStatusCode.OK);
		} else {
			response = new ErrorResponse(HttpStatusCode.NOT_FOUND,
					"User not found");
		}

		return response;
	}
}
