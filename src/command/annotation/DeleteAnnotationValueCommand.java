package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.Response;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;

import response.HttpStatusCode;
import server.Debug;

/**
 * Command used to remove an annotation value.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteAnnotationValueCommand extends Command {
	private String name;
	private String value;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 4;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {
		super.setFields(uri, query, username, userType);
		String[] splitFields = uri.split("/");
		name = splitFields[3];
		value = splitFields[4];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateName(value, MaxLength.ANNOTATION_VALUE, "Annotation value");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			if (db.removeDropDownAnnotationValue(name, value) != 0)
				response = new MinimalResponse(HttpStatusCode.OK);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of annotation value unsuccessful, " +
								"experiment label or value does not exist");
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of annotation value '" + value +
							"' unsuccessful due to temporary database " +
							"problems.");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of annotation value: " + value +
							"' unsuccessful. " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
