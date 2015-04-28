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
	//TODO Implement this class

	@Override
	public void setFields(String uri, String uuid) {

		/*No fields from the uri is needed, neither is the UUID. Dummy
		implementation*/
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
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
