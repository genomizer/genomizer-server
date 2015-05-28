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
 * Command used to change the name of an annotation field (label).
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutAnnotationFieldCommand extends Command {
	@Expose
	private String oldName = null;
	@Expose
	private String newName = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(oldName, MaxLength.ANNOTATION_LABEL,
				"Old annotation label");
		validateName(newName, MaxLength.ANNOTATION_LABEL,
				"New annotation label");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		Response response;

		try {
			db = initDB();
			db.changeAnnotationLabel(oldName, newName);
			response = new MinimalResponse(HttpStatusCode.OK);
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Editing annotation label '" + oldName +
							"' unsuccessful. " + e.getMessage());
		} catch (SQLException e) {
			response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Editing annotation label '" + oldName + "' unsuccessful " +
							"due to temporary database problems.");
			Debug.log("Reason: " + e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}

		return response;
	}
}
