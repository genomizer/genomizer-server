package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a delete user command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteUserCommand extends Command {

	/**
	 * Used to validate DeleteUserCommand.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return true;

	}

	/**
	 * Used to execute the actual removal of the user.
	 */
	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
