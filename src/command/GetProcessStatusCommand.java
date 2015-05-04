package command;

import java.util.LinkedList;

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
		LinkedList<ProcessCommand> processesList = workPool.getProcesses();
		LinkedList<ProcessStatus> processStatuses = new LinkedList<>();
        
		for (ProcessCommand proc : processesList) {
			processStatuses.add(workPool.getProcessStatus(proc));
		}
		return new GetProcessStatusResponse(processStatuses);
	}
}
