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

/**
 * Class used to handle removal of annotation values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteAnnotationValueCommand extends Command {
	private String name;
	private String value;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);

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
		try {
			db = initDB();
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {
				db.removeDropDownAnnotationValue(name, value);
			} else {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "The value " +
						value + " does not exist in " + name + " and can not " +
						"be deleted");
			}
		} catch (IOException | SQLException e) {
			Debug.log("Deleting annotation value " + value + " on annotation " + name +
					" failed due to database error. Reason: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Deleting annotation value " + value +
					" on annotation "+name + " failed due to database error.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}
