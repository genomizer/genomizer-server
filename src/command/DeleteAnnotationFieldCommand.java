package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle removal on an existing annotation-field.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteAnnotationFieldCommand extends Command {

	/**
	 * Constructor that initiates the class.
	 *
	 * @param restful header to set.
	 */
	public DeleteAnnotationFieldCommand(String restful) {

		header = restful;

	}

	/**
	 * Used to validate the DeleteAnnotationFieldCommand
	 * class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(header == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"field-name was missing.");
		}
		if(header.length() < 1 || header.length() >
				database.constants.MaxSize.ANNOTATION_LABEL) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation " +
					"label has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL +
					" characters long.");
		}
		if(!hasOnlyValidCharacters(header)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters for annotation. Valid characters are: " +
					validCharacters);
		}

		return true;

	}

	/**
	 * Used to execute the actual command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			ArrayList<String> annotations = db.getAllAnnotationLabels();

			if(annotations.contains(header)) {
				db.deleteAnnotation(header);
				return new MinimalResponse(200);
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST,
						"The annotation " + header + " does not exist and " +
								"can not be deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			db.close();
		}
	}

}
