package command.annotation;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to handle retrieving annotation privileges.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
@Deprecated
public class GetAnnotationPrivilegesCommand extends Command {
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
		return new MinimalResponse(HttpStatusCode.NO_CONTENT);
	}
}