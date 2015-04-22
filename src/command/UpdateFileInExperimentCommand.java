package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle updateing files in experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateFileInExperimentCommand extends Command {


	@Override
	public void validate() {
		// TODO Add code here probably
	}

	@Override
	public Response execute() {
		//TODO Add code here probably
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
