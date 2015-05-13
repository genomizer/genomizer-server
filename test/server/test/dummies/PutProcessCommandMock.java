package server.test.dummies;

import command.process.PutProcessCommand;
import org.junit.Ignore;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

@Ignore
public class PutProcessCommandMock extends PutProcessCommand {

	public PutProcessCommandMock() {
		super();

	}


	public Response execute() {
		try {
			Thread.sleep(1000);
			if (this.getUsername().equals("philge")) {
				return new ErrorResponse(HttpStatusCode.METHOD_NOT_ALLOWED, "Something bad happened");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new MinimalResponse(HttpStatusCode.CREATED);
	}

	@Override
	public void setFilePaths() {

	}

	@Override
	public String[] getFilePaths() {
		return new String[] {"file1", "file2"};
	}

}
