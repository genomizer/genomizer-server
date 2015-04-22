package command;

import java.util.Collection;

import response.GetProcessStatusResponse;
import response.Response;
import server.ErrorLogger;
import server.WorkPool;

/**
 * Fetches status of all processes that have been added to the server. Will be
 * reset when the server restarts. A process can have one of four states:
 * Waiting, Started, Finished and Crashed.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetProcessStatusCommand extends Command {

	private WorkPool workPool;

	/**
	 * Constructs a new instance of GetProcessStatusCommand using the supplied
	 * WorkHandler.
	 *
	 * @param workPool thw workPool in use by the server.
	 */
	public GetProcessStatusCommand(WorkPool workPool) {

		this.workPool = workPool;

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

		Collection<ProcessStatus> processStatus = null;

		processStatus = workPool.getProcesses().values();

		return new GetProcessStatusResponse(processStatus);
	}

}
