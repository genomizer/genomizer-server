package command;

import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle updating experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateExperimentCommand extends Command {
	//TODO Implement this class

	public UpdateExperimentCommand(String json, String expID) {

	}

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
	 */
	@Override
	public void validate() {
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}

}
