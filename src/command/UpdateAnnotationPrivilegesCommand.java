package command;

import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle updates on annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateAnnotationPrivilegesCommand extends Command {
	//TODO Implement this class

	public UpdateAnnotationPrivilegesCommand(String json, String userName) {

	}

	/**
	 * Used to validate the information needed to execute
	 * the command.
	 */
	@Override
	public void validate() {

	}


	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}

}
