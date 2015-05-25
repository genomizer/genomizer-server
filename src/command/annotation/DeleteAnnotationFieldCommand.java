package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {
		super.setFields(uri, query, username, userType);
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
			if (db.deleteAnnotation(label) != 0)
				response = new MinimalResponse(HttpStatusCode.OK);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of annotation label '" + label +
								"' unsuccessful, annotation label does not " +
								"exist.");
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of annotation label '" + label +
							"' unsuccessful due to temporary database " +
							"problems.");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of annotation label '" + label +
							"' unsuccessful. " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
