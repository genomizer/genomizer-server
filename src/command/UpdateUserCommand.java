package command;

import com.google.gson.annotations.Expose;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class UpdateUserCommand extends Command {

	@Expose
	public String old_username = null;
	@Expose
	public String old_password = null;
	@Expose
	public String new_username = null;
	@Expose
	public String new_password = null;
	@Expose
	public String new_name = null;
	@Expose
	public String new_email = null;


	@Override
	public boolean validate() {
		if(old_username == null | old_password == null | new_username == null | new_password == null) {
			return false;
		}
		return true;
	}

	@Override
	public Response execute() {



		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
