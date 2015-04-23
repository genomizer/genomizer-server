package command;

import java.util.LinkedList;

import response.GetProcessStatusResponse;
import response.Response;
import server.WorkPool;

/**
 * Fetches status of all processes that have been added to the server.
 * Will be reset when the server restarts. A process can have one
 * of four states: Waiting, Started, Finished and Crashed.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetProcessStatusCommand extends Command {

	private WorkPool workPool;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param workPool object.
	 */
	public GetProcessStatusCommand(WorkPool workPool) {

		this.workPool = workPool;

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

		LinkedList<ProcessCommand> processes = workPool.getProcesses();
		LinkedList<ProcessStatus> processStatuses = new LinkedList<>();

		for (ProcessCommand proc : processes) {
			processStatuses.add(workPool.getProcessStatus(proc));
		}

		return new GetProcessStatusResponse(processStatuses);

	}

}
