package command.process;

import command.Command;
import command.Process;
import command.UserRights;
import command.ValidateException;
import response.ProcessStatusResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.util.Calendar;
import java.util.LinkedList;

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

		Calendar pastCal = Calendar.getInstance();
		pastCal.setTimeInMillis(System.currentTimeMillis());
		pastCal.add(Calendar.DAY_OF_MONTH, -days);
        	
		Calendar startedCal = Calendar.getInstance();

		for (PutProcessCommand proc : processesList) {
			Process process = processPool.getProcessStatus(proc.getPID());
			startedCal.setTimeInMillis(process.timeStarted);

			if (startedCal.after(pastCal)) {
				getProcessStatuses.add(process);
			}
		}
		return new ProcessStatusResponse(getProcessStatuses);
	}
}
