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
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;
import server.Debug;

/**
 * Command used to change the value of an annotation field (label).
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutAnnotationValueCommand extends Command {
	@Expose
	private String name = null;
	@Expose
	private String oldValue = null;
	@Expose
	private String newValue = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(name, MaxLength.ANNOTATION_LABEL, "Annotation label");
		validateName(oldValue, MaxLength.ANNOTATION_LABEL,
				"Old annotation value");
		validateName(newValue, MaxLength.ANNOTATION_LABEL,
				"New annotation value");
		if (newValue.equals("freetext")) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Can not" +
					"rename a value to \"freetext\"");
		}

		if (oldValue.equals("freetext")) {
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Can not" +
					"rename a value from \"freetext\"");
		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			db.changeAnnotationValue(name, oldValue, newValue);
			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Editing of annotation value '" + oldValue +
							"' unsuccessful. " +e.getMessage());
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Editing annotation value '" + oldValue +
							"' unsuccessful due to temporary database " +
							"problems.");
			Debug.log("Reason: " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
