package command;

import authentication.Authenticate;
import response.LoginResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class LogoutCommand extends Command {

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	/**
	 * Used to execute the logout command.
	 */
	@Override
	public Response execute() {

		if(Authenticate.){
			//bugg if username is exactly the same as the UUID
				System.out.println("Användaren fanns");
				rsp = new LoginResponse(405, "");
		}
		
		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
