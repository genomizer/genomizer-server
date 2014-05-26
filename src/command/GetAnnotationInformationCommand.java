package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import database.DatabaseAccessor;

import response.AnnotationInformation;
import response.ErrorResponse;
import response.GetAnnotationInformationResponse;
import response.MinimalResponse;
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
		}
		catch(SQLException | IOException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not initialize db: " + e.getMessage());
		}

		try {
			a = db.getAnnotations();
		}
		catch(SQLException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not get annotations: " + e.getMessage());
		}


		Iterator<String> keys = a.keySet().iterator();
		ArrayList<String> annotation_names = new ArrayList<String>();

		while(keys.hasNext()) {
			annotation_names.add(keys.next());
		}

		for(int i = 0; i < annotation_names.size(); i++) {

			database.containers.Annotation annotationObject = null;
			ArrayList<String> values = new ArrayList<String>();

			try {
				annotationObject = db.getAnnotationObject(annotation_names.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(annotationObject.dataType == database.containers.Annotation.FREETEXT) {
				values.add("freetext");
			} else if(annotationObject.dataType == database.containers.Annotation.DROPDOWN) {
				values = (ArrayList<String>) annotationObject.getPossibleValues();
			}

			AnnotationInformation annotation = new AnnotationInformation(annotationObject.label, values, annotationObject.isRequired);
			annotations.add(annotation);

		}
		db.close();
		return new GetAnnotationInformationResponse(StatusCode.OK, annotations);
	}

}
