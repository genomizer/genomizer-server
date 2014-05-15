package command;

import java.util.HashMap;
import java.util.TreeMap;

import response.Response;
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

		TreeMap<ProcessCommand,String> processStatus = workHandler.getProcessStatus();


		return null;
	}

}
