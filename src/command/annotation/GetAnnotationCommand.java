package command.annotation;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public void validate() throws ValidateException {
		/*Validation of the information will always succeed,
		the command can not be corrupt.*/
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		ArrayList<AnnotationInformation> annotations = new ArrayList<>();
		DatabaseAccessor db = null;
		Map<String, Integer> a;
		try {
			db = initDB();
			a = db.getAnnotations();
			List<String> list = new ArrayList<>(a.keySet());
			for(String label: list) {
				database.containers.Annotation annotationObject;
				ArrayList<String> values = new ArrayList<>();
				annotationObject = db.getAnnotationObject(label);

				if(annotationObject.dataType ==
						database.containers.Annotation.FREETEXT) {
					values.add("freetext");
				} else if(annotationObject.dataType ==
						database.containers.Annotation.DROPDOWN) {
					values = (ArrayList<String>)
							annotationObject.getPossibleValues();
				}
				AnnotationInformation annotation =
						new AnnotationInformation(annotationObject.label,
								values, annotationObject.isRequired);
				annotations.add(annotation);
			}
			return new AnnotationListResponse(annotations);
		} catch(SQLException | IOException e) {
			Debug.log("Retrieval of annotation information failed. Reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Could not retrieve annotation " +
					"information because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
