package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Edits the label of an annotation. The object is generated
 * directly from JSON with parameters oldName and newName.
 * oldName must be an existing annotation label and
 * newName can't be the label of an existing annotation.
 *
 * @author ens10olm
 *
 */
public class EditAnnotationFieldCommand extends Command {

	@Expose
	private String oldName;
	@Expose
	private String newName;

	public EditAnnotationFieldCommand() {

	}

	@Override
	public boolean validate() {
		if (oldName != null && oldName.length() > 0 && newName != null && newName.length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Changes the label of annotation oldName to newName. All database entries
	 * will be affected by the change. Will return a bad request response if
	 * either parameter is invalid, and an OK response if the modification
	 * succeeded.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			Map<String,Integer> anno = db.getAnnotations();

			if (!anno.containsKey(oldName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + oldName + " does not exist in the database");
			} else if (anno.containsKey(newName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + newName + " already exists in the database");
			}
			db.changeAnnotationLabel(oldName, newName);
		} catch (IOException | SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
			}
		}

		return new MinimalResponse(StatusCode.OK);
	}

}
