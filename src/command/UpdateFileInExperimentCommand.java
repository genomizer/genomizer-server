package command;

import database.subClasses.UserMethods.UserType;
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

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uuid, userType);
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}
}
