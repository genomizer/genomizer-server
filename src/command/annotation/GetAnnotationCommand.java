package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.Annotation;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

/**
 * Class used to get information about annotations.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetAnnotationCommand extends Command {
	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void setFields(String uri, String query, String uuid,
						  UserType userType) {
		super.setFields(uri, query, uuid, userType);
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			List<String> list = new ArrayList<>(db.getAnnotations().keySet());
			for (String label : list) {
				ArrayList<String> values;
				Annotation annotation = db.getAnnotationObject(label);
				if (annotation.dataType == Annotation.FREETEXT) {
					values = new ArrayList<>();
					values.add("freetext");
				} else if  {

				}
			}
		} catch (IOException | SQLException e) {

		}

//		ArrayList<AnnotationInformation> annotations = new ArrayList<>();
//		DatabaseAccessor db = null;
//		Map<String, Integer> a;
//		try {
//			db = initDB();
//			a = db.getAnnotations();
//			List<String> list = new ArrayList<>(a.keySet());
//			for(String label: list) {
//				database.containers.Annotation annotationObject;
//				ArrayList<String> values = new ArrayList<>();
//				annotationObject = db.getAnnotationObject(label);
//
//				if(annotationObject.dataType ==
//						database.containers.Annotation.FREETEXT) {
//					values.add("freetext");
//				} else if(annotationObject.dataType ==
//						database.containers.Annotation.DROPDOWN) {
//					values = (ArrayList<String>)
//							annotationObject.getPossibleValues();
//				}
//				AnnotationInformation annotation =
//						new AnnotationInformation(annotationObject.label,
//								values, annotationObject.isRequired);
//				annotations.add(annotation);
//			}
//			return new GetAnnotationInformationResponse(HttpStatusCode.OK,
//					annotations);
//		} catch(SQLException | IOException e) {
//			Debug.log("Retrieval of annotation information failed. Reason: " +
//					e.getMessage());
//			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Could not retrieve annotation " +
//					"information because of temporary problems with database.");
//		} finally {
//			if (db != null) {
//				db.close();
//			}
//		}
	}
}
