package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle updating experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateExperimentCommand extends Command {

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
	 */
	@Override
	public void validate() {
		// TODO Add code here probably
	}

	@Override
	public Response execute() {
		//TODO Add code here probably
		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
