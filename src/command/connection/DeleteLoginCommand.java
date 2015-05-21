package command.connection;

import authentication.Authenticate;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to represent a logout command.
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
		String id = Authenticate.getID(userName);
		if(Authenticate.idExists(id)) {
			Authenticate.deleteActiveUser(id);
			return new MinimalResponse(HttpStatusCode.OK);
		} else {
			return 	new ErrorResponse(HttpStatusCode.NOT_FOUND,
					"User not found");
		}
	}
}
