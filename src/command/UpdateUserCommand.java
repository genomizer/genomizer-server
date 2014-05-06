package command;

import response.ErrorResponse;
import response.Response;
import response.StatusCode;

public class UpdateUserCommand extends Command {

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(StatusCode.NO_CONTENT);

	}

}
