package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle retrieving annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetAnnotationPrivilegesCommand extends Command {
	@Override
	public void validate() {
		// TODO Something should be added here perhaps?
	}

	@Override
	public Response execute() {
		// TODO Actually do something?
		// TODO send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
