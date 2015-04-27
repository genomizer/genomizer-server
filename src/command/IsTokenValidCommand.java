package command;

import authentication.Authenticate;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * This class is used to check if a token is valid.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class IsTokenValidCommand extends Command {
	private String uuid;

	@Override
	public void setFields(String uri, String uuid) {

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
		int code = Authenticate.idExists(uuid) ? StatusCode.OK :
				StatusCode.UNAUTHORIZED;
		return new MinimalResponse(code);
	}
}
