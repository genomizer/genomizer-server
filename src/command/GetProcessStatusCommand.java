package command;

import java.util.LinkedList;

import database.subClasses.UserMethods.UserType;
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

	private WorkPool workPool;

	/**
	 * Constructs a new instance of GetProcessStatusCommand using the supplied
	 * WorkHandler.
	 *
	 * @param workPool thw workPool in use by the server.
	 */
	public GetProcessStatusCommand(WorkPool workPool, UserType userType) {

		this.workPool = workPool;
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
		LinkedList<ProcessCommand> processesList = workPool.getProcesses();
		LinkedList<ProcessStatus> processStatuses = new LinkedList<>();
        
		for (ProcessCommand proc : processesList) {
			processStatuses.add(workPool.getProcessStatus(proc));
		}
		return new GetProcessStatusResponse(processStatuses);
	}
}
