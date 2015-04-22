package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle the process of adding annotation
 * values.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class AddAnnotationValueCommand extends Command {
	@Expose
	private String name = null;

	@Expose
	private String value = null;

	@Override
	public void validate() throws ValidateException {
		if(value == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify an " +
					"annotation value.");
		}
		if(name == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify an " +
					"annotation name.");
		}
		if(value.length() > MaxSize.ANNOTATION_VALUE) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"value has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_VALUE +
					" characters long.");
		}
		if(name.length() > MaxSize.ANNOTATION_LABEL || name.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"label has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL +
					" characters long.");
		}
		if(name.indexOf('/') != -1 || !hasOnlyValidCharacters(name)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in annotation name. Valid characters are: " +
					validCharacters);
		}
		if(value.indexOf('/') != -1 || !hasOnlyValidCharacters(value)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in annotation value. Valid characters are: " +
					validCharacters);
		}
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {
				db.close();
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The " +
						"annotation " + name + " already contains the value " +
						value);
			}
			db.addDropDownAnnotationValue(name, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
 		}

		return new MinimalResponse(StatusCode.CREATED);
	}

}
