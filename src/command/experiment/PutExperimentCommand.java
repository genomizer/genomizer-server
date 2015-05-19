package command.experiment;

import command.Command;
import command.UserRights;
import command.ValidateException;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle updating experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutExperimentCommand extends Command {
	//TODO Implement this class


	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
	 */
	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.METHOD_NOT_YET_IMPLEMENTED);
	}

}
