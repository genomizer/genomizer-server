package command;

import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RemoveExperimentCommand extends Command {

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(StatusCode.NO_CONTENT);

	}

}
