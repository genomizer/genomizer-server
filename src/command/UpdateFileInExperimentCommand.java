package command;

import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle updateing files in experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateFileInExperimentCommand extends Command {
	//TODO Implement this class

	public UpdateFileInExperimentCommand(String json, String expID) {

	}

	@Override
	public void validate() {
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}
}
