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
	//TODO Implement this class

	@Override
	public void setFields(String uri, String uuid) {

		/*No fields from the uri is needed, neither is the UUID. Dummy
		implementation*/
	}

	public GetAnnotationPrivilegesCommand(String userName) {

	}

	@Override
	public void validate() {

	}

	@Override
	public Response execute() {
		return new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
