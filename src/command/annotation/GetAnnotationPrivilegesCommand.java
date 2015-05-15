package command.annotation;

import command.Command;
import command.UserRights;
import command.ValidateException;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to handle retrieving annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
@Deprecated
public class GetAnnotationPrivilegesCommand extends Command {
	//TODO Implement this class


	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
