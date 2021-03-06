package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.Annotation;
import response.*;
import server.Debug;

/**
 * Command used to get information about annotations.
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
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			ArrayList<AnnotationInformation> annotations = new ArrayList<>();
			List<String> possibleAnnotations = new ArrayList<>(db.
					getAnnotations().keySet());
			for (String label : possibleAnnotations) {
				ArrayList<String> values;
				Annotation annotation = db.getAnnotationObject(label);
				if (annotation.dataType == Annotation.DROPDOWN) {
					values = (ArrayList<String>) annotation.getPossibleValues();
				} else {
					values = new ArrayList<>();
					values.add("freetext");
				}

				annotations.add(new AnnotationInformation(annotation.label,
						values, annotation.isRequired));
			}

			response = new AnnotationListResponse(annotations);
		} catch (IOException | SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Retrieval of annotation information unsuccessful due " +
							"to temporary database problems.");
			Debug.log("Reason: " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
