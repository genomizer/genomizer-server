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
			a = db.getAnnotations();
		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		}

		Iterator<String> keys = a.keySet().iterator();
		ArrayList<String> annotation_names = new ArrayList<String>();
		while(keys.hasNext()) {
			annotation_names.add(keys.next());
		}

		for(int i = 0; i < annotation_names.size(); i++) {
			ArrayList<String> values = null;
			try {
				if(db.getAnnotationType(annotation_names.get(i)) == DatabaseAccessor.FREETEXT) {
					values = new ArrayList<String>();
					values.add("freetext");
				} else if(db.getAnnotationType(annotation_names.get(i)) == DatabaseAccessor.DROPDOWN) {
					values = (ArrayList<String>) db.getChoices(annotation_names.get(i));
				} else {

				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}

			AnnotationInformation annotation = new AnnotationInformation(0, annotation_names.get(i), values, true);
			annotations.add(annotation);
		}

	    ArrayList<String> vals = new ArrayList<String>();
	    vals.add("freetext");
	    AnnotationInformation expId = new AnnotationInformation(0, "ExpID", vals, false);
		annotations.add(expId);

		Collections.sort(annotations, new compareAnnotations());

		for(int i = 0; i < annotations.size(); i++) {
			annotations.get(i).setId(i);
		}

		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new GetAnnotationInformationResponse(200, annotations);
	}

	private class compareAnnotations implements Comparator<AnnotationInformation> {

		@Override
		public int compare(AnnotationInformation arg0,
				AnnotationInformation arg1) {

			return arg0.getName().compareTo(arg1.getName());
		}
	}

}
