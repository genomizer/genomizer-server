package command.annotation;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * Class used to handle updates on annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
@Deprecated
public class PutAnnotationPrivilegesCommand extends Command {
	//TODO Implement this class

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	/**
	 * Used to validate the information needed to execute
	 * the command.
	 */
	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}


	@Override
	public Response execute() {
		return 	new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}

}
