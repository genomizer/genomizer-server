package command;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import response.GetProcessStatusResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.WorkHandler;

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
