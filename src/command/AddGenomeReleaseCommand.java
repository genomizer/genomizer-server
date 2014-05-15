package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {


	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		return false;
	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		Response rsp = null;

		rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);


		return rsp;
	}

}
