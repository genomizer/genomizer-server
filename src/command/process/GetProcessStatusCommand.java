package command.process;

import command.Command;
import command.UserRights;
import command.ValidateException;
import response.ProcessStatusResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

/**
 * Fetches status of all processes that have been added to the server. Will be
 * reset when the server restarts. A process can have one of four states:
 * Waiting, Started, Finished and Crashed.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetProcessStatusCommand extends Command {

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	/**
	 * Method that validates the class information.
	 */
	@Override
	public void validate() throws ValidateException {
		/*Validation will always succeed for the content,
		the command can not be corrupt.*/
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {

		ProcessPool processPool = Doorman.getProcessPool();
		return new ProcessStatusResponse(processPool.getProcesses());
	}
}
