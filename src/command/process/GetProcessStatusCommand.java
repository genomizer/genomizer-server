package command.process;

import java.util.LinkedList;

import command.Command;
import command.Process;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import response.GetProcessStatusResponse;
import response.Response;
import server.Doorman;
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
	public void setFields(String uri, String uuid, UserType userType) {
		
		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
		this.userType = userType;
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

		WorkPool workPool = Doorman.getWorkPool();
		LinkedList<PutProcessCommand> processesList = workPool.getProcesses();
		LinkedList<Process> getProcessStatuses = new LinkedList<>();
        
		for (PutProcessCommand proc : processesList) {
			getProcessStatuses.add(workPool.getProcessStatus(proc));
		}
		return new GetProcessStatusResponse(getProcessStatuses);
	}
}
