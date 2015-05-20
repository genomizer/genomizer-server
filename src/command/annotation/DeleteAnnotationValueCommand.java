package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

import javax.xml.crypto.Data;

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
	public void setFields(String uri, String query, String uuid,
						  UserType userType) {
		super.setFields(uri, query, uuid, userType);
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
			if (db.getChoices(name).contains(value)) {
				db.removeDropDownAnnotationValue(name, value);
				response = new MinimalResponse(HttpStatusCode.OK);
			} else {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of annotation value " + value +
								" unsuccessful, value " + value +
								"does not exist in " + name + ".");
			}
		} catch (IOException | SQLException e) {
			Debug.log("Deletion of annotation value: " + value +
					" was unsuccessful, reason: " + e.getMessage());
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Deletion of annotation value: " + value +
							" was unsuccessful due to temporary problems with" +
							" the database.");
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
