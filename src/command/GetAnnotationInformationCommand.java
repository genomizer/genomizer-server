package command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import databaseAccessor.DatabaseAccessor;

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

		DatabaseAccessor accessor = null;
		Map<String, String> a = null;
		try {
			accessor = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
			a = accessor.getAnnotations();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<String> keys = a.keySet().iterator();
		ArrayList<String> annotation_names = new ArrayList<String>();
		while(keys.hasNext()) {
			annotation_names.add(keys.next());
		}

		for(int i = 0; i < annotation_names.size(); i++) {
			int type;
			if(a.get(annotation_names.get(i)).equals("dropdown")) {
				type = AnnotationInformation.TYPE_DROP_DOWN;
			} else {
				type = AnnotationInformation.TYPE_FREE_TEXT;
			}
			ArrayList<String> values = null;
			try {
				values = accessor.getDropDownAnnotations(annotation_names.get(i));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AnnotationInformation annotation = new AnnotationInformation(0, annotation_names.get(i), type, values, true);
			annotations.add(annotation);
		}
		Collections.sort(annotations, new compareAnnotations());

		for(int i = 0; i < annotations.size(); i++) {
			annotations.get(i).setId(i);
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
