package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a create user command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CreateUserCommand extends Command {

	/**
	 * Used to validate the CreateUserCommand.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return true;

	}

	/**
	 * Used to execute the actual creation of the user.
	 */
	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
