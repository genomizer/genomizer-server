package command.test;

import response.ErrorResponse;
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
			Thread.sleep(2000);
			if (this.getAuthor().equals("philge")) {
				return new ErrorResponse(StatusCode.METHOD_NOT_ALLOWED, "Something bad happened");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new MinimalResponse(StatusCode.CREATED);
	}

	@Override
	public void setFilePaths() {

	}

	@Override
	public String[] getFilePaths() {
		return new String[] {"file1", "file2"};
	}

}
