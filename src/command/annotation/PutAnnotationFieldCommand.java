package command.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.annotations.Expose;

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

/**
 * Edits the label of an annotation. The object is generated directly from
 * JSON with parameters oldName and newName. oldName must be an existing
 * annotation label and newName can't be the label of an existing annotation.
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
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(oldName, MaxLength.ANNOTATION_LABEL,
				"Old annotation label");
		validateName(newName, MaxLength.ANNOTATION_LABEL,
				"New annotation label");
	}

	/**
	 * Changes the label of annotation oldName to newName. All database entries
	 * will be affected by the change. Will return a bad request response if
	 * either parameter is invalid, and an OK response if the modification
	 * succeeded.
	 *
	 * @return an appropriate minimal response signaling that the edit was
	 * done successfully.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db;
		try {
			db = initDB();
		} catch(SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
					"initialize db: " + e.getMessage());
		}
		try {
			Map<String,Integer> anno = db.getAnnotations();
			if (!anno.containsKey(oldName)) {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "The " +
						"annotation field " + oldName + " does not exist in " +
						"the database");
			} else if (anno.containsKey(newName)) {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "The " +
						"annotation field " + newName + " already exists in " +
						"the database");
			}
			try {
				db.changeAnnotationLabel(oldName, newName);
			} catch (IOException | SQLException e) {
				return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
						"change annotation label: " + e.getMessage());
			}

		} catch(SQLException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
					"get annotations: " + e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return new MinimalResponse(HttpStatusCode.OK);
	}
}