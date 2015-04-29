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

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
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
