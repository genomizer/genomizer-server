package command;

import authentication.Authenticate;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class IsTokenValidCommand extends Command {

	private String uuid;

	public IsTokenValidCommand(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Response execute() {
		int code = Authenticate.idExists(uuid) ? StatusCode.OK : StatusCode.UNAUTHORIZED;
		return new MinimalResponse(code);
	}

}
