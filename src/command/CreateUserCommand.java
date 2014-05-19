package command;

import com.google.gson.annotations.Expose;

import database.MaxSize;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

//TODO: Add better validation.

/**
 * Class used to represent a create user command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CreateUserCommand extends Command {

	@Expose
	private String username = null;

	@Expose
	private String password = null;

	@Expose
	private String privileges = null;

	@Expose
	private String name = null;

	@Expose
	private String email = null;

	/**
	 * Used to validate the CreateUserCommand.
	 */
	@Override
	public boolean validate() {

		if(username == null || password == null || privileges == null) {
			return false;
		}

		if(username.length() < 1 || username.length() > MaxSize.USERNAME) {
			return false;
		}

		if(password.length() < 1 || password.length() > MaxSize.PASSWORD) {
			return false;
		}

		if(privileges.length() < 1 || privileges.length() > MaxSize.ROLE) {
			return false;
		}

		return true;

	}

	/**
	 * Used to execute the actual creation of the user.
	 */
	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}