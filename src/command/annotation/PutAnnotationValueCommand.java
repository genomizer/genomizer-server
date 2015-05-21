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
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			if (db.getAllAnnotationLabels().contains(name)) {
				if (db.getChoices(name).contains(oldValue)) {
					if (db.getChoices(name).contains(newValue)) {
						response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
								"Editing annotation value was unsuccessful, " +
										"the annotation value " + newValue +
										" already exists");
					} else {
						db.changeAnnotationValue(name, oldValue, newValue);
						response = new MinimalResponse(HttpStatusCode.OK);
					}
				} else {
					response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
							"Editing annotation value was unsuccessful, the " +
									"annotation value " + oldValue +
									" does not exist");
				}
			} else {
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Editing annotation value was unsuccessful, the " +
								"annotation field " + name +
								" does not exist.");
			}
		} catch (IOException | SQLException e) {
			Debug.log("Editing of annotation value " + oldValue +
					" was unsuccessful, reason : " + e.getMessage());
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Editing of annotation value " + oldValue +
							" was unsuccessful due to temporary problems with" +
							" the database");
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
