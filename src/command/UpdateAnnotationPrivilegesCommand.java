package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle updates on annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateAnnotationPrivilegesCommand extends Command {

	/**
	 * Used to validate the information needed to execute
	 * the command.
	 */
	@Override
	public void validate() {
		// TODO Add validation code here
	}


	@Override
	public Response execute() {
		//TODO Add execute code here probably
		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
