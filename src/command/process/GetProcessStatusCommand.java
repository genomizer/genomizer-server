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

	// Number of days in the past to retrieve processes
	private int days = 30;

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
		LinkedList<PutProcessCommand> processesList = processPool.getProcesses();
		LinkedList<Process> getProcessStatuses = new LinkedList<>();

		long currentTime = System.currentTimeMillis();
		long timeWeekAgo = currentTime - 60*60*24*days*1000;
        
		for (PutProcessCommand proc : processesList) {
			Process process = processPool.getProcessStatus(proc.getPID());
			if (process.timeStarted >= timeWeekAgo) {
				getProcessStatuses.add(process);
			}
		}
		return new GetProcessStatusResponse(getProcessStatuses);
	}
}
