package command.connection;

import authentication.Authenticate;
import command.Command;
import command.ValidateException;
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

	}

	@Override
	public Response execute() {
		if(Authenticate.idExists(uuid)) {
			Authenticate.deleteActiveUser(uuid);
			return new MinimalResponse(HttpStatusCode.OK);
		} else {
			return 	new ErrorResponse(HttpStatusCode.NOT_FOUND,
					"User not found");
		}
	}
}
