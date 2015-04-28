package command;

import database.subClasses.UserMethods.UserType;
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

	public UpdateExperimentCommand(String json, String expID, UserType userType) {

		this.userType = userType;
	}

	/**
	 * Used to validate the information that is needed
	 * to execute the actual command.
	 */
	@Override
	public void validate() throws ValidateException {
		hasRights(UserType.USER);
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
