package command;
import response.LoginResponse;
import response.Response;
import authentication.Authenticate;

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

		if(Authenticate.userExists(username)) {
			System.err.println("userexists: " + Authenticate.getID(username));
			return new LoginResponse(200, Authenticate.getID(username));
		} else {
			String usrId = Authenticate.createUserID(username);
			System.err.println("userdoesnotexists: " + usrId + "usrname: " + username);
			Authenticate.addUser(username,usrId);
			return new LoginResponse(200, usrId);
		}
	}

}
