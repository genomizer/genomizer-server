package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxSize;

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
	public boolean validate() throws ValidateException {
		if (oldName == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify the old annotation name");
		} else if(newName == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify the old annotation name");
		} else if(oldName.length() > MaxSize.ANNOTATION_LABEL || oldName.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Old annotation name has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL + " characters long.");
		} else if(newName.length() > MaxSize.ANNOTATION_LABEL || newName.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "New annotation name has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL + " characters long.");
		} else if(!hasOnlyValidCharacters(oldName)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation label. Valid characters are: a-z, A-Z, 0-9");
		} else if(!hasOnlyValidCharacters(newName)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation label. Valid characters are: a-z, A-Z, 0-9");
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
		}
		catch(SQLException | IOException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not initialize db: " + e.getMessage());
		}

		try {
			Map<String,Integer> anno = db.getAnnotations();

			if (!anno.containsKey(oldName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + oldName + " does not exist in the database");
			} else if (anno.containsKey(newName)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation field " + newName + " already exists in the database");
			}

			try {
				db.changeAnnotationLabel(oldName, newName);
			} catch (IOException | SQLException e) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not change annotation label: " + e.getMessage());
			}

		}
		catch(SQLException e){
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not get annotations: " + e.getMessage());
		}finally{
			db.close();
		}

		return new MinimalResponse(StatusCode.OK);
	}

}
