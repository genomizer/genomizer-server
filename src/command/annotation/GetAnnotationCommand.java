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
import database.subClasses.UserMethods.UserType;
import response.*;

/**
 * Class used to get information about annotations.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetAnnotationCommand extends Command {
	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		/*Validation of the information will always succeed,
		the command can not be corrupt.*/
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		ArrayList<AnnotationInformation> annotations = new ArrayList<AnnotationInformation>();
		DatabaseAccessor db = null;
		Map<String, Integer> a;
		try {
			db = initDB();
			a = db.getAnnotations();
			List<String> list = new ArrayList<String>(a.keySet());
			for(String label: list) {
				database.containers.Annotation annotationObject;
				ArrayList<String> values = new ArrayList<String>();
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
			return new GetAnnotationInformationResponse(HttpStatusCode.OK,
					annotations);
		} catch(SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Could not initialize db: " + e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}