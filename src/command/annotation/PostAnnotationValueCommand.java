package command.annotation;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

/**
 * Command used to add an annotation value.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationValueCommand extends Command {
	@Expose
	private String name = null;
	@Expose
	private String value = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateName(value, MaxLength.ANNOTATION_VALUE, "Annotation value");
		if(value.equals("freetext")){
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Can not" +
					" name a value \"freetext\"");
		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			db.addDropDownAnnotationValue(name, value);
			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Adding annotation value '" + value + "' unsuccessful " +
							"due to temporary database problems");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Adding " +
					"annotation value '" + value + "' unsuccessful. " +
					e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
