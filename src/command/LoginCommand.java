package command;
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
 * @author Kommunikation/kontroll 2014.
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

	/**
	 * Method used to validate the information needed in order
	 * to execute the command.
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(username == null || password == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Username and/or password was missing.");

		} else if(username.length() < 1 || username.length() > database.constants.MaxSize.USERNAME) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Username has to be between 1 and "+ database.constants.MaxSize.USERNAME+" characters long.");

		} else if(password.length() < 1 || password.length() > database.constants.MaxSize.PASSWORD) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Password has to be between 1 and "+database.constants.MaxSize.PASSWORD+" characters long.");

		}

		return true;
	}

	/**
	 * Method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		LoginAttempt login = Authenticate.login(username, password);

		if(login.wasSuccessful()) {
			Debug.log("LOGIN WAS SUCCESSFUL FOR: "+ username + ". GAVE UUID: " + Authenticate.getID(username));
			return new LoginResponse(200, login.getUUID());
		} else {
			Debug.log("LOGIN WAS UNSUCCESSFUL FOR: " + username + ". REASON: " + login.getErrorMessage());
			return new ErrorResponse(StatusCode.UNAUTHORIZED, login.getErrorMessage());
		}
	}

}
