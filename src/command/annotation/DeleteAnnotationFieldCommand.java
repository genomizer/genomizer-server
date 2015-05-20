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

import javax.xml.crypto.Data;

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
		validateName(label, MaxLength.ANNOTATION_LABEL, "Annotation label");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			if (db.getAllAnnotationLabels().contains(label)) {
				db.deleteAnnotation(label);
				response = new MinimalResponse(HttpStatusCode.OK);
			} else {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"The deletion of annotation label " + label +
								" was unsuccessful, annotation label does " +
								"not exist.");
			}
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of annotation label: " + label +
					" was unsuccessful, reason: " + e.getMessage());
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of annotation label: " + label +
							" was unsuccessful due to temporary problems with" +
							" the database.");
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
