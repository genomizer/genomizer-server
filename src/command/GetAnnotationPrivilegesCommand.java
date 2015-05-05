package command;

import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to handle retrieving annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetAnnotationPrivilegesCommand extends Command {
	//TODO Implement this class

	public GetAnnotationPrivilegesCommand(String userName) {

	}

	@Override
	public void validate() {

	}

	@Override
	public Response execute() {
		return new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}
}
