package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import database.DatabaseAccessor;

import response.AnnotationInformation;
import response.ErrorResponse;
import response.GetAnnotationInformationResponse;
import response.Response;
import response.StatusCode;

public class GetAnnotationInformationCommand extends Command {

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Response execute() {

		ArrayList<AnnotationInformation> annotations = new ArrayList<AnnotationInformation>();

		DatabaseAccessor db = null;
		Map<String, Integer> a = null;
		try {
			db = initDB();
			a = db.getAnnotations();

			List<String> list = new ArrayList<String>(a.keySet());

			for(String label: list) {
				database.containers.Annotation annotationObject = null;
				ArrayList<String> values = new ArrayList<String>();
				annotationObject = db.getAnnotationObject(label);

				if(annotationObject.dataType == database.containers.Annotation.FREETEXT) {
					values.add("freetext");
				} else if(annotationObject.dataType == database.containers.Annotation.DROPDOWN) {
					values = (ArrayList<String>) annotationObject.getPossibleValues();
				}

				AnnotationInformation annotation = new AnnotationInformation(annotationObject.label, values, annotationObject.isRequired);
				annotations.add(annotation);
			}

			ArrayList<String> values = new ArrayList<String>();
			values.add("freetext");
			AnnotationInformation expId = new AnnotationInformation("expID", values, false);
			annotations.add(expId);

			db.close();
			return new GetAnnotationInformationResponse(StatusCode.OK, annotations);
		}
		catch(SQLException | IOException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not initialize db: " + e.getMessage());
		}
	}
}
