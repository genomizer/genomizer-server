package command;

import java.util.ArrayList;

import response.AnnotationInformation;
import response.GetAnnotationInformationResponse;
import response.Response;

public class GetAnnotationInformationCommand extends Command {

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {

		ArrayList<AnnotationInformation> annotations = new ArrayList<AnnotationInformation>();







		return new GetAnnotationInformationResponse(200, null);
	}

}
