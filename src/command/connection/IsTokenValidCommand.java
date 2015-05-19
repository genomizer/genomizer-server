package command.connection;

import authentication.Authenticate;
import command.Command;
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

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
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
