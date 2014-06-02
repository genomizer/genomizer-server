package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle updateing experiments.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class UpdateExperimentCommand extends Command {

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
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

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
