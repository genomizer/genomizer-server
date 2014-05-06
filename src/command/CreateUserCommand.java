package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CreateUserCommand extends Command {

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
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
