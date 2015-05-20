package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

/**
 * Command used to handle removal of an existing annotation field.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {
	private String label;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, String query, String uuid,
						  UserType userType) {
		super.setFields(uri, query, uuid, userType);
		label = uri.split("/")[3];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(label, MaxLength.ANNOTATION_LABEL, "annotation label");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db;
		try {
			db = initDB();
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of annotation field: " + label +
					" was unsuccessful, reason: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of annotation field: " + label + " was " +
							"unsuccessful due to temporary problems with the " +
							"database.");
		}

		try {
			ArrayList<String> annotations = db.getAllAnnotationLabels();
			if (annotations.contains(label)) {
				db.deleteAnnotation(label);
				return new MinimalResponse(HttpStatusCode.OK);
			} else {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"The annotation " + label + " does not exist and " +
								"can not be deleted");
			}
		} catch (SQLException | IOException e) {
			Debug.log("Removal of annotation field: "+ label +" didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Removal of annotation field:"+ label +
					" didn't work because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
