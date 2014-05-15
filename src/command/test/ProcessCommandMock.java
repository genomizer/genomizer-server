package command.test;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import command.ProcessCommand;

public class ProcessCommandMock extends ProcessCommand {

	public ProcessCommandMock() {
		super();

	}


	public Response execute() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
