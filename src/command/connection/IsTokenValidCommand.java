package command.connection;

import authentication.Authenticate;
import command.Command;
import database.subClasses.UserMethods.UserType;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

/**
* This class is used to check if a token is valid.
*
* @author Business Logic 2015.
* @version 1.1
*/
@Deprecated
public class IsTokenValidCommand extends Command {
	private String uuid;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	/**
	 * Constructs a new instance of IsTokenValidCommand using the supplied
	 * uuid.
	 * @param uuid the unique user identification.
	 */
	public IsTokenValidCommand(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public void validate() {
		/*Validation will always succeed, the command can not be corrupt.*/
	}

	@Override
	public Response execute() {
		int code = Authenticate.idExists(uuid) ? HttpStatusCode.OK :
				HttpStatusCode.UNAUTHORIZED;
		return new MinimalResponse(code);
	}
}