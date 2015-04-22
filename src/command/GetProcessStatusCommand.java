package command;

import java.util.Collection;

import response.GetProcessStatusResponse;
import response.Response;
import server.WorkHandler;

/**
 * Fetches status of all processes that have been added to the server.
 * Will be reset when the server restarts. A process can have one
 * of four states: Waiting, Started, Finished and Crashed.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetProcessStatusCommand extends Command {
	private WorkHandler workHandler;

	/**
	 * Constructs a new instance of GetProcessStatusCommand using the supplied
	 * WorkHandler.
	 * @param workHandler the WorkHandler in use by the server.
	 */
	public GetProcessStatusCommand(WorkHandler workHandler) {
		this.workHandler = workHandler;
	}

	/**
	 * Method that validates the class information.
	 * This method always returns true.
	 */
	@Override
	public void validate() {
		/*Validation will always succeed, the command can not be corrupt.*/
	}

	@Override
	public Response execute() {
		Collection<ProcessStatus> processStatus =
				workHandler.getProcessStatus();
		return new GetProcessStatusResponse(processStatus);

	}

}
