package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import response.Response;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;

import response.StatusCode;

/**
 * Class used to handle removal of annotation values.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteAnnotationValueCommand extends Command {

	private String name;
	private String value;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param name is the label on annotation that has the value.
	 * @param value to delete.
	 */
	public DeleteAnnotationValueCommand(String name, String value) {

		this.name = name;
		this.value = value;

	}

	/**
	 * Method used to validate the DeleteAnnotationValueCommand
	 * class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(name == null || value == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"label and/or value was missing.");
		} else if(name.length() < 1 || name.length() >
				database.constants.MaxSize.ANNOTATION_LABEL) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"label has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL +
					" characters long.");
		} else if(value.length() < 1 || value.length() >
				database.constants.MaxSize.ANNOTATION_VALUE) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"value has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_VALUE +
					" characters long.");
		} else if(!hasOnlyValidCharacters(name)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in annotation label. Valid characters are: " +
					validCharacters);
		} else if(!hasOnlyValidCharacters(value)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in annotation value. Valid characters are: " +
					validCharacters);
		}
		return true;
	}

	/**
	 * Method used to execute the actual command.
	 */
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
			db.close();
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
