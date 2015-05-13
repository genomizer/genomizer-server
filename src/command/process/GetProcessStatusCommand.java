package command.process;

import java.util.LinkedList;

import command.Command;
import command.Process;
import command.UserRights;
import command.ValidateException;
import response.GetProcessStatusResponse;
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
		LinkedList<PutProcessCommand> processesList = processPool.getProcesses();
		LinkedList<Process> getProcessStatuses = new LinkedList<>();
        
		for (PutProcessCommand proc : processesList) {
			getProcessStatuses.add(processPool.getProcessStatus(proc.getPID()));
		}
		return new GetProcessStatusResponse(getProcessStatuses);
	}
}
