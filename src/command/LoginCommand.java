package command;
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

	private Authenticate authenticate;

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
	public void execute() {

		authenticate.createUserID(username);
		// TODO Auto-generated method stub

	}

}
