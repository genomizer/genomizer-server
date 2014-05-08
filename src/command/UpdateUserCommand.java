package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class UpdateUserCommand extends Command {

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);

	}

}
