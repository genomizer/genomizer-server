package command;
import response.ErrorResponse;
import response.LoginResponse;
import response.Response;
import response.StatusCode;
import authentication.Authenticate;
import authentication.LoginAttempt;

import com.google.gson.annotations.Expose;

/**
 * Command used for login.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class LoginCommand extends Command {

	@Expose
	private String username;

	@Expose
	private String password;

	/**
	 * Empty constructor.
	 */
	public LoginCommand() {

	}

	@Override
	public boolean validate() {

		if(username ==null || password==null){
			return false;
		}else if(username.length()<1 || password.length()<4){
			return false;
		}
		return true;
	}

	@Override
	public Response execute() {

		LoginAttempt login = Authenticate.login(username, password);

		if(login.wasSuccessful()) {
			System.err.println("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " + Authenticate.getID(username));
			return new LoginResponse(200, login.getUUID());
		} else {
			System.err.println("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " + login.getErrorMessage());
			return new ErrorResponse(StatusCode.BAD_REQUEST, login.getErrorMessage());
		}
	}

}
