package command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.DatabaseAccessor;

import response.AnnotationInformation;
import response.GetAnnotationInformationResponse;
import response.Response;
import server.DatabaseSettings;

public class GetAnnotationInformationCommand extends Command {

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Response execute() {

		ArrayList<AnnotationInformation> annotations = new ArrayList<AnnotationInformation>();

		DatabaseAccessor accessor = null;
		Map<String, Integer> a = null;

		try {
			accessor = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			a = accessor.getAnnotations();
			System.out.println("Got annotations.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		Iterator<String> keys = a.keySet().iterator();
		ArrayList<String> annotation_names = new ArrayList<String>();
		while(keys.hasNext()) {
			annotation_names.add(keys.next());
		}

		for(int i = 0; i < annotation_names.size(); i++) {
			ArrayList<String> values = null;
			try {
				if(accessor.getAnnotationType(annotation_names.get(i)) == DatabaseAccessor.FREETEXT) {
					values = new ArrayList<String>();
					values.add("freetext");
				} else if(accessor.getAnnotationType(annotation_names.get(i)) == DatabaseAccessor.DROPDOWN) {
					values = (ArrayList<String>) accessor.getChoices(annotation_names.get(i));
				} else {

				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			AnnotationInformation annotation = new AnnotationInformation(0, annotation_names.get(i), values, true);
			annotations.add(annotation);
		}
		Collections.sort(annotations, new compareAnnotations());

		for(int i = 0; i < annotations.size(); i++) {
			annotations.get(i).setId(i);
		}

		for(int i = 0; i < annotations.size(); i++) {
			System.out.println("\n" + annotations.get(i));

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
