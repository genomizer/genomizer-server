package command;

import authentication.Authenticate;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * This class is used to check if a token is valid.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class IsTokenValidCommand extends Command {

	private String uuid;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param uuid user identification
	 */
	public IsTokenValidCommand(String uuid) {

		this.uuid = uuid;

	}

	/**
	 * Method used to validate the information in the command.
	 * This method always returns true.
	 */
	@Override
	public boolean validate() {

		return true;

	}

	/**
	 * Method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		int code = Authenticate.idExists(uuid) ? StatusCode.OK :
				StatusCode.UNAUTHORIZED;
		return new MinimalResponse(code);

	}

}
