package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import database.constants.MaxLength;
import response.Response;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;

import response.StatusCode;

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
	public void setFields(String uri, String uuid) {
		String[] splitFields = uri.split("/");
		name = splitFields[2];
		value = splitFields[3];
	}

	@Override
	public void validate() throws ValidateException {
		validateString(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateString(value, MaxLength.ANNOTATION_VALUE, "Annotation value");
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
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The value " +
						value + " does not exist in " + name + " and can not " +
						"be deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.NO_CONTENT, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(StatusCode.OK);
	}
}
