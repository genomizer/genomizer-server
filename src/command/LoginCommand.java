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

	/* This class responds on success only
	 * with header = 200 (OK).
	 * It handles both login/logut (if needed)
	 */
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

		Response rsp;

		if(Authenticate.userExists(username)){
		//bugg if username is exactly the same as the UUID
			System.out.println("USER ALREADY EXISTS, SENDS UUID ONCE MORE.");
			rsp = new LoginResponse(200, Authenticate.getID(username));
		}else{
			String usrId = Authenticate.createUserID(username);

			Authenticate.addUser(username,usrId);
			System.out.println("USER CREATED.");
			rsp = new LoginResponse(200, usrId);
		}
			// TODO Auto-generated method stub


		return rsp;
	}

}
