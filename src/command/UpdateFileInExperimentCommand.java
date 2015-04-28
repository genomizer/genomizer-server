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
	//TODO Implement this class

	@Override
	public void setFields(String uri, String uuid) {

		/*No fields from the uri is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() {

	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
