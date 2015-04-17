package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle retrieving annotation privileges.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetAnnotationPrivilegesCommand extends Command {

	/**
	 * Empty constructor.
	 */
	public GetAnnotationPrivilegesCommand() {

	}

	/**
	 * Used to validate the GetAnnotationPrivilegesCommand
	 * class.
	 *
	 * @return always true
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return true;

	}

	/**
	 * Used to execute the actual command.
	 */
	@Override
	public Response execute() {

		// TODO send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
