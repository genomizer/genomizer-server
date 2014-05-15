package command;

import database.DatabaseAccessor;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to delete a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommand extends Command {

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
		DatabaseAccessor db;


		//Add implementation code.

		rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

		return rsp;

	}

}
