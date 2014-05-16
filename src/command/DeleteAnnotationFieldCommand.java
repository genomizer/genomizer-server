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
	 * Used to validate the logout command.
	 * Checks if all annotations which are to
	 * be deleted are present in the database.
	 */
	@Override
	public boolean validate() {
		if(header == null) {
			return false;
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
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Could not close database connection");
			}
		}
	}

}
