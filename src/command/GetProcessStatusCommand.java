package command;

import java.util.LinkedList;

import response.GetProcessStatusResponse;
import response.Response;
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

	@Override
	public void setFields(String uri, String uuid) {

		/*No fields from the uri is needed, neither is the UUID. Dummy
		implementation*/
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
		//TODO Retrieve the right workpool
		WorkPool workPool = null;
		LinkedList<ProcessCommand> processesList = workPool.getProcesses();
		LinkedList<ProcessStatus> processStatuses = new LinkedList<>();
        
		for (ProcessCommand proc : processesList) {
			processStatuses.add(workPool.getProcessStatus(proc));
		}
		return new GetProcessStatusResponse(processStatuses);
	}
}
