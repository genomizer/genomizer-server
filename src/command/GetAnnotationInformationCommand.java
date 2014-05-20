package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import database.DatabaseAccessor;
import response.AnnotationInformation;
import response.ErrorResponse;
import response.GetAnnotationInformationResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle get annotation information from the
 * database.
 *
 * @author
 * @version 1.0
 */
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
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Database error");
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database error");
		}

		Iterator<String> keys = a.keySet().iterator();
		ArrayList<String> annotation_names = new ArrayList<String>();
		while(keys.hasNext()) {
			annotation_names.add(keys.next());
		}

		for(int i = 0; i < annotation_names.size(); i++) {

			database.Annotation annotationObject = null;
			ArrayList<String> values = new ArrayList<String>();

			try {
				annotationObject = db.getAnnotationObject(annotation_names.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(annotationObject.dataType == database.Annotation.FREETEXT) {
				values.add("freetext");
			} else if(annotationObject.dataType == database.Annotation.DROPDOWN) {
				values = (ArrayList<String>) annotationObject.getPossibleValues();
			}

			AnnotationInformation annotation = new AnnotationInformation(annotationObject.label, values, annotationObject.isRequired);
			annotations.add(annotation);

		}

		/*ArrayList<String> vals = new ArrayList<String>();
	    vals.add("freetext");
	    AnnotationInformation expId = new AnnotationInformation(0, "ExpID", vals, false);
		annotations.add(expId);*/

		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close database connection");
		}
		return new GetAnnotationInformationResponse(200, annotations);
	}

}
