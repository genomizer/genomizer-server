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
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetProcessStatusCommand extends Command {

	private WorkHandler workHandler;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param workHandler object.
	 */
	public GetProcessStatusCommand(WorkHandler workHandler) {

		this.workHandler = workHandler;

	}

	/**
	 * Method that validates the class information.
	 * This method always returns true.
	 */
	@Override
	public boolean validate() {

		return true;

	}

	/**
	 * Method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		Collection<ProcessStatus> processStatus =
				workHandler.getProcessStatus();
		return new GetProcessStatusResponse(processStatus);

	}

}
