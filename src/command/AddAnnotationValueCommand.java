package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a logout command.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationValueCommand extends Command {

	@Expose
	private String name;

	@Expose
	private String value;

	/**
	 * Used to validate the logout command.
	 */
	@Override
	public boolean validate() {
		if(value == null || name == null) {
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
			db.addDropDownAnnotationValue(name, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return new MinimalResponse(StatusCode.NO_CONTENT);
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database unavailable");
		} finally{
			try {
				db.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return new MinimalResponse(StatusCode.NO_CONTENT);
			}
		}
		return new MinimalResponse(StatusCode.OK);
	}

}
