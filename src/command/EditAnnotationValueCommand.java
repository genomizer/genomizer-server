package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.constants.MaxSize;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

public class EditAnnotationValueCommand extends Command {

	@Expose
	String name;

	@Expose
	String oldValue;

	@Expose
	String newValue;

	@Override
	public boolean validate() throws ValidateException {
		if(name == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify an annotation label.");
		}
		if(oldValue == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify the old annotation value.");
		}
		if(newValue == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify the new annotation value.");
		}
		if(name.length() > MaxSize.ANNOTATION_LABEL || name.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation label has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES + " characters long.");
		}
		if(oldValue.length() > MaxSize.ANNOTATION_VALUE || oldValue.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Old annotation value has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES + " characters long.");
		}
		if(newValue.length() > MaxSize.ANNOTATION_VALUE || newValue.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "New annotation value has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES + " characters long.");
		}
		if(!hasOnlyValidCharacters(name)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation label. Valid characters are: " + validCharacters);
		}
		if(!hasOnlyValidCharacters(oldValue)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in old annotation value. Valid characters are: " + validCharacters);
		}
		if(!hasOnlyValidCharacters(newValue)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in new annotation value. Valid characters are: " + validCharacters);
		}
		return true;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;

		try {
			db = initDB();
			ArrayList<String> annotations = db.getAllAnnotationLabels();
			if(annotations.contains(name)) {
				List<String> values = db.getChoices(name);
				if(values.contains(oldValue)) {
					db.changeAnnotationValue(name, oldValue, newValue);
					return new MinimalResponse(StatusCode.OK);
				} else {
					return new ErrorResponse(StatusCode.BAD_REQUEST, "The value" + oldValue + " does not exist");
				}
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + name + " does not");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			db.close();
		}
	}

}
