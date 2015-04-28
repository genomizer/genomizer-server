package command;

import database.subClasses.UserMethods.UserType;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle updates on annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class UpdateAnnotationPrivilegesCommand extends Command {
	//TODO Implement this class

	public UpdateAnnotationPrivilegesCommand(String json, String userName, UserType userType) {
		this.userType = userType;
	}

	/**
	 * Used to validate the information needed to execute
	 * the command.
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
