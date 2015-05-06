package command;

import java.util.LinkedList;

import response.GetProcessStatusResponse;
import response.Response;
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

	private ProcessPool processPool;

	/**
	 * Constructs a new instance of GetProcessStatusCommand using the supplied
	 * WorkHandler.
	 *
	 * @param processPool the processPool in use by the server.
	 */
	public GetProcessStatusCommand(ProcessPool processPool) {

		this.processPool = processPool;

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
		LinkedList<ProcessCommand> processesList = processPool.getProcesses();
		LinkedList<ProcessStatus> processStatuses = new LinkedList<>();
        
		for (ProcessCommand proc : processesList) {
			processStatuses.add(processPool.getProcessStatus(proc.getPID()));
		}
		return new GetProcessStatusResponse(processStatuses);
	}
}
