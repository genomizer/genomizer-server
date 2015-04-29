package command;

import database.subClasses.UserMethods.UserType;
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

	public UpdateFileInExperimentCommand(String json, String expID, UserType userType) {
	this.userType = userType;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}
}
