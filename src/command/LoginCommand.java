package command;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.LoginResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import authentication.Authenticate;
import authentication.LoginAttempt;

import com.google.gson.annotations.Expose;

/**
 * This class is used to handle user login.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class LoginCommand extends Command {
	@Expose
	private String username = null;

	@Expose
	private String password = null;

	@Override
	public void validate() throws ValidateException {
		validateString(username, MaxSize.USERNAME, "Username/Password");
		validateString(password, MaxSize.PASSWORD, "Username/Password");
	}

	@Override
	public Response execute() {
		LoginAttempt login = Authenticate.login(username, password);
		if(login.wasSuccessful()) {
			Debug.log("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " +
					Authenticate.getID(username));
			return new LoginResponse(200, login.getUUID());
		}
		Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " +
				login.getErrorMessage());
		return new ErrorResponse(StatusCode.UNAUTHORIZED,
				login.getErrorMessage());
	}
}
