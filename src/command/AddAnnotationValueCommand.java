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
 * Class used to represent a logout command.
 *
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
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
		if(value.length() < 1 || value.length() > MaxSize.ANNOTATION_VALUE) {
			return false;
		}
		if(name.length() < 1 || name.length() > MaxSize.ANNOTATION_LABEL) {
			return false;
		}
		if(name.indexOf('/') != -1) {
			return false;
		}
		if(value.indexOf('/') != -1) {
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
			List<String> values = db.getChoices(name);
			if(values.contains(value)) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The annotation " + name + " already contains the value " + value);
			}
			db.addDropDownAnnotationValue(name, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database unavailable");
		} finally{
			db.close();
		}
		return new MinimalResponse(StatusCode.CREATED);
	}

}
