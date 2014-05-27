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
 * Class used to represent a logout command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class DeleteAnnotationFieldCommand extends Command {

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

			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation field-name was missing.");

		} else if(header.length() < 1 || header.length() > database.constants.MaxSize.ANNOTATION_LABEL) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Annotation field-name has to be between 1 and "
					+ database.constants.MaxSize.ANNOTATION_LABEL + " characters long.");

		}

		return true;

	}

	/**
	 * Used to execute the logout command.
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
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + header + " does not exist and can not be deleted");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} finally {
			db.close();
		}
	}

}
