package command;

import java.util.Collection;
import response.GetProcessStatusResponse;
import response.Response;
import server.WorkHandler;

/**
 * Fetches status of all processes that have been added to the server.
 * Will be reset when the server restarts. A process can have one
 * of four states: Waiting, Started, Finished and Crashed.
 * @author ens10olm
 *
 */
public class GetProcessStatusCommand extends Command {

	private WorkHandler workHandler;

	public GetProcessStatusCommand(WorkHandler workHandler) {
		this.workHandler = workHandler;
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Response execute() {
		Collection<ProcessStatus> processStatus = workHandler.getProcessStatus();
		return new GetProcessStatusResponse(processStatus);
	}

}
