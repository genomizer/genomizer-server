package command;

import java.io.IOException;
import java.sql.SQLException;
import database.DatabaseAccessor;
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
			db.deleteAnnotation(header);
			db.close();
			return new MinimalResponse(200);
		} catch (SQLException e) {
			System.out.println("ERROR CODE: " + e.getErrorCode());
			e.printStackTrace();
			System.out.println("ERROR MESS: "+ e.getMessage());
			return new MinimalResponse(StatusCode.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
			}
		}
	}

}
